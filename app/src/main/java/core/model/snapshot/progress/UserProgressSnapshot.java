package core.model.snapshot.progress;

import java.util.Map;

import core.model.snapshot.Snapshot;

public record UserProgressSnapshot(
    Map<String, Object> value
) implements Snapshot {

}
