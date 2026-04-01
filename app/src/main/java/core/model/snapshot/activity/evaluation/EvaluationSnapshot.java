package core.model.snapshot.activity.evaluation;

import core.model.snapshot.Snapshot;
import java.util.Map;

public record EvaluationSnapshot (
    Map<String, Object> values
) implements Snapshot {

}
