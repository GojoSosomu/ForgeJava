package presentation.service.assembler;

import core.model.snapshot.activity.ActivitySnapshot;
import core.model.view.activity.ActivityView;

public class ActivityViewAssembler implements ViewAssembler<ActivitySnapshot, ActivityView> {
    private ProblemViewAssembler problemViewAssembler;

    public ActivityViewAssembler(
        ProblemViewAssembler problemViewAssembler
    ) {
        this.problemViewAssembler = problemViewAssembler;
    }

    @Override
    public ActivityView from(ActivitySnapshot snapshot) {
        return new ActivityView(
            snapshot.id(),
            problemViewAssembler.from(snapshot.problem())
        );
    }

}
