package presentation.outside.swing;

import javax.swing.JPanel;

import core.model.view.activity.ActivityView;
import infrastructure.event.receiver.ScoreReceiver;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.activity.SwingActivityResultPanel;
import presentation.outside.swing.template.activity.SwingQuestionnaireTemplate;
import presentation.service.ActivityService;
import presentation.service.UserService;

public class SwingActivity {
    private final SwingLauncher launcher;
    private final SwingActivityResultPanel resultUI;
    private final JPanel activityUI;

    public SwingActivity(UserService userService, ActivityService service, ActivityView activityViewv, SwingLauncher launcher, Runnable onFinish) {
        this.launcher = launcher;
        
        // 1. Create the Channel (The Score Screen)
        this.resultUI = new SwingActivityResultPanel(onFinish);
        
        // 2. Create the Receiver
        ScoreReceiver receiver = new ScoreReceiver(this.resultUI);

        // 3. Create the Factory Floor (The Template)
        this.activityUI = switch (activityViewv.problemView().type()) {
            case QUESTIONNAIRE -> new SwingQuestionnaireTemplate(
                service, activityViewv, launcher, 
                (scoreView) -> {
                    userService.completedActivityItem(activityViewv.id(), scoreView.score(), scoreView.total());
                    // PNEUMATIC TUBE: Send score to receiver
                    receiver.onPulse(scoreView);
                    // SWITCH UI: Show the results now
                    this.showResults(); 
                }
            );
        };
    }

    public void show() { launcher.switchPanel(activityUI, 1200, 820); }
    public void showResults() { launcher.switchPanel(resultUI); }
}