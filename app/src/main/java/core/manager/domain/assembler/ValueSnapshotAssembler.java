package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.List;

import core.model.snapshot.Snapshot;

public interface ValueSnapshotAssembler<T, R extends Snapshot> {
    default List<R> from(List<? extends T> objects) {
        List<R> result = new ArrayList<>();

        for(T object : objects) {
            result.add(this.from(object));
        }

        return result;
    }
    R from(T object);
}
