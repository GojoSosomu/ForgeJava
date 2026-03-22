package presentation.outside.swing.template.activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import core.model.view.activity.ActivityView;
import core.model.view.activity.problem.QuestionnaireView;
import core.model.view.progress.info.ScoreView;
import infrastructure.event.pulse.Pulse;
import core.model.view.activity.problem.QuestionPageView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.page.SwingQuestionnairePage;
import presentation.service.ActivityService;
import presentation.utility.ActivitySession;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingQuestionnaireTemplate extends JPanel implements SwingActivityTemplate {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pageContainer = new JPanel(cardLayout);
    private final JLabel attemptLabel;
    private final JLabel pageCounterLabel;
    private final CarouselUtility<QuestionPageView> carousel;
    private final ActivitySession activitySession = new ActivitySession();
    private final List<SwingQuestionnairePage> questionPages = new ArrayList<>();
    private final Pulse<ScoreView> onFinish;
    private final ActivityService service;
    private final SwingLauncher launcher; // Fixed: Needs to be final
    private final String id;

    public SwingQuestionnaireTemplate(
        ActivityService service, 
        ActivityView activityView, 
        SwingLauncher launcher, 
        Pulse<ScoreView> onFinish
    ) {
        this.launcher = launcher; 
        this.service = service;
        this.onFinish = onFinish;
        this.id = activityView.id();
        
        QuestionnaireView questionnaire = (QuestionnaireView) activityView.problemView();
        this.carousel = new CarouselUtility<>(questionnaire.questions());
        this.carousel.setWrapAround(false);

        setLayout(new BorderLayout());
        setOpaque(false);

        // --- TOP HEADER (Attempt Counter) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        header.setOpaque(false);
        attemptLabel = new JLabel("ATTEMPTS: " + activitySession.getAttempt());
        attemptLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        attemptLabel.setForeground(withAlpha(INK_MEDIUM, 150));
        header.add(attemptLabel);
        add(header, BorderLayout.NORTH); // Put it at the top

        // --- CENTER (Pages) ---
        pageContainer.setOpaque(false);
        // (Building pages loop remains the same...)
        for (int i = 0; i < questionnaire.questions().size(); i++) {
            SwingQuestionnairePage page = new SwingQuestionnairePage(
                activityView.id(), "Q" + (i + 1), questionnaire.questions().get(i), service, () -> moveToNext()
            );
            questionPages.add(page);
            pageContainer.add(page, "Q" + i);
        }
        add(pageContainer, BorderLayout.CENTER);

        // --- FOOTER (Page Counter) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        footer.setOpaque(false);
        pageCounterLabel = new JLabel();
        pageCounterLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        pageCounterLabel.setForeground(withAlpha(INK_MEDIUM, 180));
        footer.add(pageCounterLabel);
        add(footer, BorderLayout.SOUTH);

        if (!questionnaire.questions().isEmpty()) {
            cardLayout.show(pageContainer, "Q0");
        }
        
        updatePageCounter(); 
        setUpGestureListener();
    }

    @Override
    public void start() {
        requestFocusInWindow();
    }

    private void setUpGestureListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        launcher.returnChapterSequence();
                        break;
                }
            }
        });
    }

    private void moveToNext() {
        if (carousel.moveRight()) {
            cardLayout.show(pageContainer, "Q" + carousel.getCurrentIndex());
            updatePageCounter();
            repaint();
        } else {
            // 1. Calculate the final score on the floor
            int total = questionPages.size();
            int finalScore = 0;
            for (SwingQuestionnairePage page : questionPages) {
                if (page.isCorrect()) finalScore++;
            }

            // 2. Wrap it into a ScoreView using your finalize method
            ScoreView result = scoreFinilize(finalScore, total);

            if(result.score() == result.total()) {
                onFinish.onPulse(result);
            } else {
                if(activitySession.shouldRetry())
                    launcher.startActivity(id);
                else
                    onFinish.onPulse(result);
                    launcher.returnChapterSequence();
            }
            // 3. Fire the Pulse!
             
        }
    }
    private void updatePageCounter() {
        int current = carousel.getCurrentIndex() + 1;
        int total = carousel.size();
        pageCounterLabel.setText("DATA UNIT: " + current + " / " + total);
        attemptLabel.setText("ATTEMPTS LEFT: " + activitySession.getAttempt());
    }

    @Override
    public ScoreView scoreFinilize(int score, int total) {
        String status = service.checkStatus(score, total);
        return new ScoreView(
            score,
            total,
            status
        );
    }
}