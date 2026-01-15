package core.model.snapshot.activity;

import core.model.snapshot.Snapshot;
import core.model.snapshot.activity.problem.ProblemSnapshot;

public record ActivitySnapshot(
    String id,
    ProblemSnapshot problem
) implements Snapshot {

}
