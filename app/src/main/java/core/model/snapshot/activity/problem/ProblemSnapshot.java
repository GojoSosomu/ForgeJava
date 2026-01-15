package core.model.snapshot.activity.problem;

import java.util.Map;

import core.model.dto.activity.problem.ProblemType;
import core.model.snapshot.Snapshot;

public record ProblemSnapshot(
    ProblemType type,
    Map<String, Object> value
) implements Snapshot {

}
