package core.model.snapshot.activity.evaulation;

import core.model.snapshot.Snapshot;
import java.util.Map;

public record EvaulationSnapshot (
    Map<String, Object> values
) implements Snapshot {

}
