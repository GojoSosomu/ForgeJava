package core.model.dto.activity.problem;

import java.util.List;

import core.model.dto.activity.problem.question.Question;
import core.model.dto.content.TextContent;

public record Questionnaire(
    List<TextContent> instructions, 
    List<Question> questions
) implements Problem {
    
    @Override
    public ProblemType type() {
        return ProblemType.QUESTIONNAIRE;
    }
}
