package presentation.outside.launcher;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import infrastructure.event.receiver.LoadingReceiver;
import presentation.outside.channel.OutsideChannel;
import presentation.outside.console.ConsoleLoading;
import presentation.service.assembler.ViewAssembler;

public class ConsoleLauncher extends Launcher {
    
    @Override
    public void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler) {
        OutsideChannel<LoadingView> loadingBarRenderer = new ConsoleLoading();
        
        LoadingReceiver loadingReceiver = new LoadingReceiver(
            loadingBarRenderer,
            viewAssembler
        );

        bootService.boot(loadingReceiver);
    }
}
