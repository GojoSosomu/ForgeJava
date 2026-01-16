package presentation.service.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.dto.content.ContentType;
import core.model.snapshot.activity.ActivitySnapshot;
import core.model.snapshot.chapter.ChapterSnapshot;
import core.model.snapshot.content.ContentSnapshot;
import core.model.snapshot.lesson.LessonSnapshot;
import core.model.view.chapter.*;
import core.model.view.content.TextContentView;

public class ChapterViewAssembler implements ViewAssembler<ChapterSnapshot, ChapterView> {
    private ContentViewAssembler contentViewAssembler;
    private LessonViewAssembler lessonViewAssembler;
    private ActivityViewAssembler activityViewAssembler;

    public ChapterViewAssembler(
        ContentViewAssembler contentViewAssembler,
        LessonViewAssembler lessonViewAssembler,
        ActivityViewAssembler activityViewAssembler
    ) {
        this.contentViewAssembler = contentViewAssembler;
        this.lessonViewAssembler = lessonViewAssembler;
        this.activityViewAssembler = activityViewAssembler;
    }

    @Override
    public ChapterView from(ChapterSnapshot snapshot) {
        Map<String, Object> values = snapshot.values();
        return new ChapterView(
            snapshot.id(),
            assembleCard((Map<String, Object>) values.get("card")),
            assembleIntro((Map<String, Object>) values.get("intro")),
            assembleSequence(
                (List<String>) values.get("sequence"),
                (Map<String, LessonSnapshot>) values.get("lessons"),
                (Map<String, ActivitySnapshot>) values.get("activities")
            ),
            assembleOutro((Map<String, Object>) values.get("outro")));
    }

    private ChapterOutroView assembleOutro(Map<String,Object> part) {
        return new ChapterOutroView(
            (String) part.get("title"), 
            (String) part.get("description"), 
            contentViewAssembler.from((List<ContentSnapshot>) part.get("sneakPeaks")), 
            contentViewAssembler.from((List<ContentSnapshot>) part.get("conclusion")));
    }

    private ChapterSequenceView assembleSequence(
        List<String> sequence,
        Map<String, LessonSnapshot> lessons,
        Map<String, ActivitySnapshot> activities) {
        
        return new ChapterSequenceView(
            lessonViewAssembler.from(lessons), 
            activityViewAssembler.from(activities), 
            sequence);
    }

    private ChapterIntroView assembleIntro(Map<String,Object> part) {
        List<TextContentView> description = new ArrayList<>();
        List<TextContentView> objectives = new ArrayList<>();

        for(ContentSnapshot snapshot : (List<ContentSnapshot>) part.get("description"))
            if(snapshot.type() == ContentType.TEXT)
                description.add((TextContentView) contentViewAssembler.from(snapshot));

        for(ContentSnapshot snapshot : (List<ContentSnapshot>) part.get("objectives"))
            if(snapshot.type() == ContentType.TEXT)
                objectives.add((TextContentView) contentViewAssembler.from(snapshot));

        return new ChapterIntroView(
            (String) part.get("title"), 
            description, 
            objectives);
    }

    private ChapterCardView assembleCard(Map<String, Object> part) {
        return new ChapterCardView(
            (String) part.get("title"), 
            (String) part.get("subTitle"), 
            (TextContentView) contentViewAssembler.from((ContentSnapshot) part.get("message")));
    }

    
}
