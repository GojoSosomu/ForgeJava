package presentation.outside.swing;

import javax.swing.JPanel;

import core.model.view.activity.ActivityView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.activity.SwingActivityTemplate;
import presentation.outside.swing.template.activity.SwingQuestionnaireTemplate;
import presentation.service.ActivityService;

public class SwingActivity {
    private final SwingLauncher launcher;

    private final JPanel activityUI;

    public SwingActivity(
        ActivityService service,
        ActivityView view, 
        SwingLauncher launcher,
        Runnable onFinish
    ) {
        this.launcher = launcher;

        this.activityUI = switch (view.problemView().type()) {
            case QUESTIONNAIRE -> new SwingQuestionnaireTemplate(service, view, launcher, onFinish);
        };
    }

    public void show() {
        launcher.switchPanel(activityUI);
        if (activityUI instanceof SwingActivityTemplate t) t.start();
    }
}