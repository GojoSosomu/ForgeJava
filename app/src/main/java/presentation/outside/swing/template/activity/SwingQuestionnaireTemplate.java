package presentation.outside.swing.template.activity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import core.model.view.activity.ActivityView;
import core.model.view.activity.problem.QuestionnaireView;
import core.model.view.activity.problem.QuestionPageView;
import presentation.outside.swing.template.page.SwingQuestionnairePage;
import presentation.utility.CarouselUtility;

public class SwingQuestionnaireTemplate extends JPanel implements SwingActivityTemplate {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pageContainer = new JPanel(cardLayout);
    private final CarouselUtility<QuestionPageView> carousel;
    private final List<SwingQuestionnairePage> questionPages = new ArrayList<>();
    private final Runnable onFinish;

    public SwingQuestionnaireTemplate(ActivityView view, presentation.outside.launcher.SwingLauncher launcher, Runnable onFinish) {
        this.onFinish = onFinish;
        QuestionnaireView questionnaire = (QuestionnaireView) view.problemView();
        
        this.carousel = new CarouselUtility<>(questionnaire.questions());
        this.carousel.setWrapAround(false);

        setLayout(new BorderLayout());
        setOpaque(false);
        pageContainer.setOpaque(false);

        // Build the physical pages
        for (int i = 0; i < questionnaire.questions().size(); i++) {
            SwingQuestionnairePage page = new SwingQuestionnairePage(questionnaire.questions().get(i));
            questionPages.add(page);
            pageContainer.add(page, "Q" + i);
        }

        add(pageContainer, BorderLayout.CENTER);
        
        // Navigation logic (Keys or Buttons) would go here
    }

    @Override
    public void start() {
        requestFocusInWindow();
    }
    
    // ... Implement logic for checking and moving to next question ...
}