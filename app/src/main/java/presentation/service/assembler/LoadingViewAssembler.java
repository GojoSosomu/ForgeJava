package presentation.service.assembler;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<LoadingView> from(List<LoadingSnapshot> snapshots) {
        List<LoadingView> result = new ArrayList<>();

        for(LoadingSnapshot snapshot : snapshots) {
            result.add(this.from(snapshot));
        }

        return result;
    }
}

