package core.manager.domain.assembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.snapshot.Snapshot;

public interface EntitySnapshotAssembler<V extends Snapshot> {
    default Map<String, V> from(List<String> ids) {
        Map<String, V> result = new HashMap<>();

        for(String id : ids) {
            result.put(id, this.from(id));
        }

        return result;
    }
    V from(String id);
}
