package core.model.view.activity.problem;

import java.util.List;

import core.model.dto.activity.problem.ProblemType;
import core.model.view.content.TextContentView;

public record QuestionnaireView(
    List<TextContentView> instructions,
    List<QuestionPageView> questions
) implements ProblemView {

    @Override
    public ProblemType type() {
        return ProblemType.QUESTIONNAIRE;
    }
    
}
