package presentation.outside.swing;

import javax.swing.JPanel;

import core.model.view.activity.ActivityView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.activity.SwingActivityTemplate;
import presentation.outside.swing.template.activity.SwingQuestionnaireTemplate;

public class SwingActivity {
    private final SwingLauncher launcher;
    private final ActivityView view;
    private final Runnable onFinish;

    // The logic selects the physical template
    private final JPanel activityUI;

    public SwingActivity(ActivityView view, SwingLauncher launcher, Runnable onFinish) {
        this.view = view;
        this.launcher = launcher;
        this.onFinish = onFinish;

        // Factory Logic: Pick the JPanel template
        this.activityUI = switch (view.problemView().type()) {
            case QUESTIONNAIRE -> new SwingQuestionnaireTemplate(view, launcher, onFinish);
            // Add more as the Metropolis grows...
        };
    }

    public void show() {
        launcher.switchPanel(activityUI);
        // If the panel has custom start logic (like focus)
        if (activityUI instanceof SwingActivityTemplate t) t.start();
    }
}