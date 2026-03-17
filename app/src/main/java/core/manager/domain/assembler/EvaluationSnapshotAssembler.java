package core.manager.domain.assembler;

import java.util.HashMap;
import java.util.Map;
import core.manager.domain.ActivityManager;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;

public class EvaluationSnapshotAssembler implements ValueSnapshotAssembler<ActivityManager.Evaulation, EvaulationSnapshot> {

    @Override
    public EvaulationSnapshot from(ActivityManager.Evaulation domainResult) {
        // Create the Map for the Snapshot
        Map<String, Object> values = new HashMap<>();

        // Pack the evaluated truth into the Map
        values.put("correctKey", domainResult.correctKey());
        values.put("correctAnswer", domainResult.correctAnswer());
        values.put("isCorrect", domainResult.isCorrect());

        // Return the standardized Snapshot
        return new EvaulationSnapshot(values);
    }
}