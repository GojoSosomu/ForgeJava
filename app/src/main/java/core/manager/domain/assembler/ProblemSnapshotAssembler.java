package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.Problem;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
import core.model.dto.activity.problem.question.QuestionType;
import core.model.snapshot.activity.problem.ProblemSnapshot;

public class ProblemSnapshotAssembler implements ValueSnapshotAssembler<Problem, ProblemSnapshot> {
    private ContentSnapshotAssembler contentSnapshotAssembler = new ContentSnapshotAssembler();

    @Override
    public List<ProblemSnapshot> from(List<? extends Problem> objects) {
        List<ProblemSnapshot> result = new ArrayList<>();

        for(Problem object : objects) {
            result.add(this.from(object));
        }

        return result;
    }

    @Override
    public ProblemSnapshot from(Problem object) {
        return switch (object.type()) {
            case QUESTIONNAIRE -> fromProblem((Questionnaire) object);
        };
    }

    private ProblemSnapshot fromProblem(Questionnaire questionnaire) {
        Map<String, Object> values = new HashMap<>();

        values.put("instructions", 
            contentSnapshotAssembler.from(questionnaire.instructions()));
        values.put("questions", 
            buildQuestions(questionnaire.questions()));

        return new ProblemSnapshot(questionnaire.type(), values);

    }

    private List<Map<String, Object>> buildQuestions(List<Question> questions) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Question question : questions) {
            Map<String,Object> values = new HashMap<>();

            values.put("questionNumber", 
                question.questionNumber());
            values.put("type", 
                question.type());
            values.put("question", 
                contentSnapshotAssembler.from(question.question())
                );
            
            switch((QuestionType) values.get("type")) {
                case MULTIPLE_CHOICE:
                    values.put("options",
                        question.values().get("options"));
                    values.put("correctedIndex",
                        question.values().get("correctedIndex"));
                    break;
                case TEXT:
                    values.put("correctedAnswer",
                        question.values().get("correctedIndex"));
                    break;
            }
        }

        return result;
    }
}
