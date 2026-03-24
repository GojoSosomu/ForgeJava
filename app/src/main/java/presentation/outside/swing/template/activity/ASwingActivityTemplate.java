package presentation.outside.swing.template.activity;

import javax.swing.JPanel;

import core.model.view.View;
import core.model.view.progress.info.ScoreView;
import infrastructure.event.pulse.Pulse;
import presentation.outside.interfaces.IActivityTemplate;
import presentation.outside.launcher.SwingLauncher;
import presentation.service.ActivityService;

public abstract class ASwingActivityTemplate extends JPanel implements IActivityTemplate {
    protected final Pulse<ScoreView> onFinish;
    protected final ActivityService service;
    protected final SwingLauncher launcher; // Fixed: Needs to be final
    protected final String id;

    public ASwingActivityTemplate(
        ActivityService service, 
        SwingLauncher launcher, 
        Pulse<ScoreView> onFinish,
        String id
    ) {
        this.service = service;
        this.launcher = launcher;
        this.onFinish = onFinish;
        this.id = id;
    }

    public abstract View getResultView();
}
