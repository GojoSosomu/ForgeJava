package presentation.service;

import java.util.List;
import java.util.Map;

import core.engine.Engine;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;
import core.model.view.activity.ActivityView;
import core.model.view.activity.evaulation.EvaulationView;
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
        System.out.println(engine.getActivitys().get(id));
        return viewAssembler.from(engine.getActivitys().get(id));
    }

    public EvaulationView evaluate(String id, int qIndex, int userChoice, List<String> options) {
        // 1. Ask the Brain to inspect the answer
        EvaulationSnapshot snapshot = engine.evaluateActivityAnswer(id, qIndex, userChoice);
        
        // 2. Translate Snapshot -> View (Protection logic)
        Map<String, Object> values = snapshot.values();
        boolean isCorrect = (boolean) values.get("isCorrect");
        int correctIndex = (int) values.get("correctIndex");

        // The Service produces the View that tells the GUI how to react
        return new EvaulationView(
            isCorrect ? "CORRECTED!" : "LOGICAL ERROR: TRUTH MISMATCH -> " + options.get(correctIndex),
            isCorrect,
            correctIndex
        );
    }
}
