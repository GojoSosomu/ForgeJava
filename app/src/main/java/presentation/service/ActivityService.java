package presentation.service;

import java.util.Map;

import core.engine.Engine;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;
import core.model.view.activity.ActivityView;
import core.model.view.activity.evaulation.EvaulationView;
import presentation.service.assembler.ActivityViewAssembler;
import presentation.utility.ActivitySession;

public class ActivityService extends AService {
    private ActivityViewAssembler viewAssembler;
    private ActivitySession activitySession;

    public ActivityService(
        Engine engine,
        ActivityViewAssembler viewAssembler,
        ActivitySession activitySession
    ) {
        super(engine);

        this.viewAssembler = viewAssembler;
        this.activitySession = activitySession;
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
        return new EvaulationView(
            isCorrect ? "CORRECTED!" : "LOGICAL ERROR",
            isCorrect
        );
    }

    public String checkStatus(int score, int total) {
        return (score == total) ? "MASTERED!!" : Math.floor(score * (3/4) * (100)) >= 75 ? "TRY AGAIN" : "FAILED" ;
    }

    public void attemptCountDown() {
        activitySession.decrementAttempt();
    }
}
