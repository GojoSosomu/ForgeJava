package presentation.service.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.snapshot.Snapshot;
import core.model.view.View;

public interface ViewAssembler<T extends Snapshot,R extends View> {
    default List<R> from(List<T> snapshots) {
        List<R> result = new ArrayList<>();

        for(T snapshot : snapshots) {
            result.add(this.from(snapshot));
        }

        return result;
    }

    default Map<String, R> from(Map<String, T> snapshots) {
        Map<String, R> result = new HashMap<>();

        for(Map.Entry<String, T> snapshot : snapshots.entrySet()) {
            result.put(snapshot.getKey(), this.from(snapshot.getValue()));
        }

        return result;
    }

    R from(T snapshot);
}
