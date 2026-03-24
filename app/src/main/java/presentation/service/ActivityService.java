package presentation.service;

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
        return viewAssembler.from(engine.getActivitys().get(id));
    }

    public EvaulationView evaluate(String id, String qNumber, String userChoice) {
        // 1. Ask the Brain to inspect the answer
        EvaulationSnapshot snapshot = engine.evaluateActivityAnswer(id, qNumber, userChoice);
        
        // 2. Translate Snapshot -> View (Protection logic)
        Map<String, Object> values = snapshot.values();
        boolean isCorrect = (boolean) values.get("isCorrect");

        // The Service produces the View that tells the GUI how to react
        System.out.println((String)values.get("correctAnswer"));
        return new EvaulationView(
            isCorrect ? "CORRECTED!" : "LOGIC INCORRECTED!",
            isCorrect ? "CORRECTED!" : ((String)values.get("correctAnswer")),
            isCorrect
        );
    }

    public String checkStatus(int score, int total) {
        return (score == total) ? "MASTERED!!" : Math.floor(score * (3/4) * (100)) >= 75 ? "TRY AGAIN" : "FAILED" ;
    }
}
