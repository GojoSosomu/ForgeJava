package presentation.service;

import core.engine.Engine;
import core.model.view.activity.ActivityView;
import presentation.service.assembler.ActivityViewAssembler;

public class ActivityService extends AService {
    private ActivityViewAssembler viewAssembler;

    public ActivityService(
        Engine engine,
        ActivityViewAssembler viewAssembler
    ) {
        super(engine);

        this.viewAssembler = viewAssembler;
    }

    public ActivityView getActivity(String id) {
        return viewAssembler.from(engine.getActivitys().get(id));
    }
}
