package presentation.outside.launcher;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import presentation.service.BootService;
import presentation.service.assembler.ViewAssembler;

public abstract class Launcher {
    BootService bootService = new BootService();

    public abstract void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler);
}
