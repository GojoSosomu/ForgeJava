package core.model.view.activity.problem;

import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.question.QuestionType;
import core.model.view.View;
import core.model.view.content.TextContentView;

public record QuestionPageView(
    String questionNumber,
    QuestionType type,
    List<TextContentView> question,
    Map<String, Object> extras
) implements View {

}
