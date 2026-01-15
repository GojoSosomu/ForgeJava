package core.manager.domain.assembler;

import java.util.List;

import core.model.snapshot.Snapshot;

public interface ValueSnapshotAssembler<T, R extends Snapshot> {
    List<R> from(List<? extends T> objects);
    R from(T object);
}
