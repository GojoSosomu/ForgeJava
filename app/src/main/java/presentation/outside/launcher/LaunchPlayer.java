package presentation.outside.launcher;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import presentation.service.assembler.LoadingViewAssembler;
import presentation.service.assembler.ViewAssembler;

public class LaunchPlayer {
    public static void play(Launcher launcher) throws NullPointerException {
        if(launcher != null) {
            ViewAssembler<LoadingSnapshot, LoadingView> loadViewAssembler = new LoadingViewAssembler();
            launcher.start(loadViewAssembler);
        } else
            throw new NullPointerException("Attempting to launch with no object launcher");
    }
}
