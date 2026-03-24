package presentation.outside.swing;

import core.model.view.activity.ActivityView;
import core.model.view.progress.info.ScoreView;
import infrastructure.event.receiver.ScoreReceiver;
import presentation.outside.interfaces.IActivityResult;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.activity.*;
import presentation.service.ActivityService;
import presentation.service.UserService;
import presentation.utility.ActivitySessionManager;

public class SwingActivity {
    private final SwingLauncher launcher;
    private final IActivityResult resultUI;
    private final ASwingActivityTemplate activityUI;
    private final ScoreReceiver receiver;

    public SwingActivity(UserService userService, ActivityService service, ActivityView activityView, SwingLauncher launcher, Runnable onFinish) {
        this.launcher = launcher;
        
        // 1. Create the Channel (The Score Screen)
        this.resultUI = new SwingQuestionnaireResultPanel(onFinish, launcher);
        
        // 2. Create the Receiver
        receiver = new ScoreReceiver(this.resultUI);

        // 3. Create the Factory Floor (The Template)
        this.activityUI = switch (activityView.problemView().type()) {
            case QUESTIONNAIRE -> new SwingQuestionnaireTemplate(
                service, activityView, launcher, 
                (scoreView) -> {
                    if(ActivitySessionManager.getSession(activityView.id()).getAttempt() != 0)
                        userService.completedActivityItem(activityView.id(), scoreView.score(), scoreView.total());
                    else
                        userService.updateScore(activityView.id(), scoreView.score(), scoreView.total());

                    // SWITCH UI: Show the results now
                    this.showResults(scoreView);
                }
            );
        };
    }

    public void show() { launcher.switchPanel(activityUI); }

    private void showResults(ScoreView scoreView) {
        resultUI.setView(activityUI.getResultView());
        receiver.onPulse(scoreView);
        resultUI.showResult(); 
    }
}