package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.manager.domain.ActivityManager;
import core.manager.domain.ActivityManager.Evaulation;
import core.model.dto.activity.problem.Problem;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;
import core.model.snapshot.activity.problem.ProblemSnapshot;

public class EvaluationSnapshotAssembler implements ValueSnapshotAssembler<ActivityManager.Evaulation, EvaulationSnapshot>{

    @Override
    public EvaulationSnapshot from(Evaulation object) {
        return switch (object.problemType()) {
            case QUESTIONNAIRE -> fromQuestionnaireEvaulation(object);
        };
    }

    private EvaulationSnapshot fromQuestionnaireEvaulation(Evaulation evaulation) {
        Map<String, Object> values = new HashMap<>();

        return new EvaulationSnapshot(values);

    }

    private List<Map<String, Object>> buildQuestions(List<Question> questions) {
        List<Map<String, Object>> result = new ArrayList<>();

        Question question = 
            
            switch(question.type()) {
                case MULTIPLE_CHOICE:
                    values.put("options",
                        contentSnapshotAssembler.from((List<Content>)question.values().get("options")));
                    values.put("correctedIndex",
                        question.values().get("correctedIndex"));
                    break;
            }

            result.add(values);
        }

        return result;
    }

}
