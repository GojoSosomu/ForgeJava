package presentation.outside.swing.template.activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import core.model.view.activity.ActivityView;
import core.model.view.activity.problem.QuestionnaireView;
import core.model.view.activity.problem.QuestionPageView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.page.SwingQuestionnairePage;
import presentation.service.ActivityService;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingQuestionnaireTemplate extends JPanel implements SwingActivityTemplate {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pageContainer = new JPanel(cardLayout);
    private final JLabel pageCounterLabel;
    private final CarouselUtility<QuestionPageView> carousel;
    private final List<SwingQuestionnairePage> questionPages = new ArrayList<>();
    private final Runnable onFinish;
    private final SwingLauncher launcher; // Fixed: Needs to be final

    public SwingQuestionnaireTemplate(
        ActivityService service, 
        ActivityView view, 
        SwingLauncher launcher, 
        Runnable onFinish
    ) {
        this.launcher = launcher; // FIX: Assign the launcher!
        this.onFinish = onFinish;
        
        QuestionnaireView questionnaire = (QuestionnaireView) view.problemView();
        this.carousel = new CarouselUtility<>(questionnaire.questions());
        this.carousel.setWrapAround(false);

        setLayout(new BorderLayout());
        setOpaque(false);
        pageContainer.setOpaque(false);

        // Footer for counter
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        footer.setOpaque(false);
        
        pageCounterLabel = new JLabel();
        pageCounterLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        pageCounterLabel.setForeground(withAlpha(INK_MEDIUM, 180));
        footer.add(pageCounterLabel);
        add(footer, BorderLayout.SOUTH);

        // Build Pages
        for (int i = 0; i < questionnaire.questions().size(); i++) {
            SwingQuestionnairePage page = new SwingQuestionnairePage(
                questionnaire.questions().get(i),
                service,
                () -> moveToNext()
            );
            questionPages.add(page);
            pageContainer.add(page, "Q" + i);
        }

        add(pageContainer, BorderLayout.CENTER);

        if (!questionnaire.questions().isEmpty()) {
            cardLayout.show(pageContainer, "Q0");
        }
        
        updatePageCounter(); // FIX: Initialize the label
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
            onFinish.run(); // All units processed!
        }
    }

    private void updatePageCounter() {
        int current = carousel.getCurrentIndex() + 1;
        int total = carousel.size();
        pageCounterLabel.setText("DATA UNIT: " + current + " / " + total);
    }
}