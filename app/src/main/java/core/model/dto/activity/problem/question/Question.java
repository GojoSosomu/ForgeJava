package core.model.dto.activity.problem.question;

import java.util.List;
import java.util.Map;

import core.model.dto.content.TextContent;

public record Question(
    String questionNumber,
    QuestionType type,
    List<TextContent> question,
    Map<String, Object> values
) {
    
}
