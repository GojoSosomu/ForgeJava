package core.manager.domain.assembler;

import java.util.List;

import core.model.snapshot.Snapshot;

public interface EntitySnapshotAssembler<V extends Snapshot> {
    List<V> from(List<String> ids);
    V from(String id);
}
