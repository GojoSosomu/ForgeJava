package infrastructure.importer.translation.maker.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.Problem;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
import core.model.dto.activity.problem.question.QuestionType;
import core.model.dto.content.Content;
import core.model.dto.content.ContentType;
import core.model.dto.content.TextContent;
import infrastructure.importer.translation.filter.ContentFilter;
import infrastructure.importer.translation.mapper.ContentMapper;

public final class QuestionnaireMaker extends ProblemMaker {

    public QuestionnaireMaker(ContentMapper contentMapper, ContentFilter contentFilter) {
        super(contentMapper, contentFilter);
    }

    @Override
    public Problem make(Map<String, Object> raw) {
        List<TextContent> texts = new ArrayList<>();

        for(Content content : contentFilter.listByType(contentMapper.list((List<Map<String, Object>>) raw.get("instructions")), ContentType.TEXT)) {
            texts.add((TextContent)content);
        }

        List<TextContent> instructions = texts;

        List<Question> questions =
            makeQuestions((List<Map<String, Object>>) raw.get("questions"));

        return new Questionnaire(instructions, questions);
    }

    private List<Question> makeQuestions(List<Map<String, Object>> rawQuestions) {
        List<Question> result = new ArrayList<>();
        int i = 0;

        for (Map<String, Object> raw : rawQuestions) {

            Map<String, Object> values = new HashMap<>();

            values.put("questionNumber", 
                "Q" + ++i);
            values.put("type", 
                QuestionType.fromString((String) raw.get("type")));
            values.put("question", 
                contentFilter.listByType(
                    contentMapper.list((List<Map<String, Object>>) raw.get("question")), 
                    ContentType.TEXT));
            
            Map<String, Object> extraValues = new HashMap<>();
            Map<String, Object> rawValues = (Map<String, Object>) raw.get("values");
            
            values.put("values", extraValues);

            switch((QuestionType) values.get("type")) {
                case MULTIPLE_CHOICE:
                    extraValues.put("options",
                        (List<String>) rawValues.get("options"));
                    extraValues.put("correctAnswerIndex",
                        (int) rawValues.get("correctAnswerIndex"));
                    break;
                case TEXT:
                    extraValues.put("correctedAnswer",
                        (String) rawValues.get("correctedAnswer"));
                    break;
            }

            result.add(new Question(
                (String) values.get("questionNumber"),
                (QuestionType) values.get("type"),
                (List<TextContent>) values.get("question"),
                extraValues));
        }

        return result;
    }
}
