package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.Problem;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
import core.model.dto.content.Content;
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

    private List<Map<String, Object>> buildQuestions(Map<String, Question> questions) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Question question : questions.values()) {
            Map<String,Object> values = new HashMap<>();

            values.put("type", 
                question.type());
            values.put("question", 
                contentSnapshotAssembler.from(question.question())
                );
            values.put("questionNumber", question.questionNumber());
            
            switch(question.type()) {
                case MULTIPLE_CHOICE:
                    values.put("options",
                        contentSnapshotAssembler.from((Map<String, Content>)question.values().get("options")));
                    values.put("correctAnswerKey",
                        question.values().get("correctAnswerKey"));
                    break;
            }

            result.add(values);
        }

        return result;
    }
}
