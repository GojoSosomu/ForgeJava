package presentation.service.assembler;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;

public final class LoadingViewAssembler implements ViewAssembler<LoadingSnapshot, LoadingView> {
    @Override
    public LoadingView from(LoadingSnapshot snapshot) {
        return new LoadingView(
            snapshot.total(),
            snapshot.progress(),
            snapshot.name(),
            snapshot.percentage()
        );
    }
}

