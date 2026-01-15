package presentation.service.assembler;

import java.util.List;

import core.model.snapshot.Snapshot;
import core.model.view.View;

public interface ViewAssembler<T extends Snapshot,R extends View> {
    List<R> from(List<T> snapshots);
    R from(T snapshot);
}
