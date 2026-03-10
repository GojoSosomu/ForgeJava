package presentation.service.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.activity.problem.question.QuestionType;
import core.model.dto.content.enums.ContentType;
import core.model.snapshot.activity.problem.ProblemSnapshot;
import core.model.snapshot.content.ContentSnapshot;
import core.model.view.activity.problem.ProblemView;
import core.model.view.activity.problem.QuestionPageView;
import core.model.view.activity.problem.QuestionnaireView;
import core.model.view.content.TextContentView;

public class ProblemViewAssembler implements ViewAssembler<ProblemSnapshot, ProblemView> {
    private ContentViewAssembler contentViewAssembler;

    public ProblemViewAssembler(
        ContentViewAssembler contentViewAssembler
    ) {
        this.contentViewAssembler = contentViewAssembler;
    }

    @Override
    public ProblemView from(ProblemSnapshot snapshot) {
        Map<String, Object> values = snapshot.value();

        List<TextContentView> instruction = new ArrayList<>();

        for(ContentSnapshot contentSnapshot : (List<ContentSnapshot>) values.get("instructions"))
            if(contentSnapshot.type() == ContentType.TEXT)
                instruction.add((TextContentView) contentViewAssembler.from(contentSnapshot));

        return switch (snapshot.type()) {
            case QUESTIONNAIRE -> new QuestionnaireView(
                    instruction, 
                    assembleQuestions((List<Map<String, Object>>) values.get("questions"))
                );
        };
    }

    private List<QuestionPageView> assembleQuestions(List<Map<String, Object>> questions) {
        List<QuestionPageView> result = new ArrayList<>();

        for(Map<String, Object> question : questions) {
            result.add(this.assembleQuestion(question));
        }

        return result;
    }

    private QuestionPageView assembleQuestion(Map<String,Object> question) {
        List<TextContentView> questionDirection = new ArrayList<>();

        for(ContentSnapshot snapshot : (List<ContentSnapshot>) question.get("question"))
            if(snapshot.type() == ContentType.TEXT)
                questionDirection.add((TextContentView) contentViewAssembler.from(snapshot));

        return new QuestionPageView(
            (String) question.get("questionNumber"),
            (QuestionType) question.get("type"),
            questionDirection,
            assembleExtraInfo(question)
        );
    }

    private Map<String, Object> assembleExtraInfo(Map<String,Object> question) {
        Map<String, Object> extraInfo = new HashMap<>();

        switch((QuestionType) question.get("type")) {
            case MULTIPLE_CHOICE -> {
                extraInfo.put("options", 
                    assembleOptions(question));
            }
            default -> {
                return extraInfo;
            }
        }
        
        return extraInfo;
    }

    private List<TextContentView> assembleOptions(Map<String,Object> question) {
        List<TextContentView> options = new ArrayList<>();

        for(ContentSnapshot snapshot : (List<ContentSnapshot>) question.get("options"))
            if(snapshot.type() == ContentType.TEXT)
                options.add((TextContentView) contentViewAssembler.from(snapshot));

        return options;
    }
}
