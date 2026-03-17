package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.snapshot.Snapshot;

public interface ValueSnapshotAssembler<T, R extends Snapshot> {
    default List<R> from(List<? extends T> objects) {
        List<R> result = new ArrayList<>();

        for(T object : objects) {
            result.add(this.from(object));
        }

        return result;
    }

    default Map<String, R> from(Map<String, ? extends T> objects) {
        Map<String, R> result = new HashMap<>();

        for(Map.Entry<String, ? extends T> entry : objects.entrySet()) {
            result.put(entry.getKey(), this.from(entry.getValue()));
        }

        return result;
    }

    R from(T object);
}
