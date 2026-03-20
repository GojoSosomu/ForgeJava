package core.model.dto.activity.problem;

import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.question.Question;
import core.model.dto.content.TextContent;

public record Questionnaire(
    List<TextContent> instructions, 
    Map<String, Question> questions
) implements Problem {
    
    @Override
    public ProblemType type() {
        return ProblemType.QUESTIONNAIRE;
    }
}
