package presentation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import core.model.view.chapter.*;
import core.engine.Engine;
import core.model.snapshot.chapter.*;
import presentation.service.assembler.ChapterViewAssembler;
import presentation.service.assembler.UserProgressAssembler;

public class ChapterService extends AService {
    private ChapterViewAssembler chapterViewAssembler;
    private UserProgressAssembler userProgressAssembler;
    private Set<String> availableChapterId;

    public ChapterService(
        ChapterViewAssembler chapterViewAssembler,
        UserProgressAssembler userProgressAssembler,
        Engine engine
    ) {
        super(engine);
        this.chapterViewAssembler = chapterViewAssembler;
        this.userProgressAssembler = userProgressAssembler;
    }

    public void onChapterSelected(String chapterID, boolean isLocked) {
        if(!isLocked) System.out.println("Hey, it get pressed on " + chapterID);
    }

    public List<String> getAllChapterID() {
        List<String> result = new ArrayList<>();

        for(ChapterView chapter : getAllChapters())
            result.add(chapter.id());

        return result;
    }

    public List<ChapterView> getAllChapters() {
        List<ChapterSnapshot> result = new ArrayList<>();

        Map<String, ChapterSnapshot> chapterSnapshots = engine.getChapters();

        Map<String, ChapterSnapshot> sortedChapterSnapshots = new TreeMap<>(chapterSnapshots);
        for(Map.Entry<String, ChapterSnapshot> chapter : sortedChapterSnapshots.entrySet())
            result.add(chapter.getValue());
        
        return chapterViewAssembler.from(result);
    }

    public void setUpAvailableChapters() {
        this.availableChapterId = availableChapterId();
    }

    private Set<String> availableChapterId() {
        List<String> allIds = getAllChapterID();

        if(engine.getCurrentUser() == null) return new HashSet<>();
        int limit = Math.min(userProgressAssembler.from(engine.getCurrentUser()).progressInfo().currentChapter(), allIds.size());
        return new HashSet<>(allIds.subList(0, limit));
    }

    public boolean isChapterAvailable(String chapterId) {
        return availableChapterId.contains(chapterId);
    }

    public ChapterView getChapter(String currentItem) {
        return chapterViewAssembler.from(engine.getChapters().get(currentItem));
    }

    public boolean isItemLocked(String chapterId, String id) {
        short sequenceIndex  = engine.getCurrentUserSequenceIndex();

        ChapterSnapshot snapshot = engine.getChapters().get(chapterId);
        Map<String, Object> value = snapshot.values();
        List<String> sequence = (List<String>)value.get("sequence");
        
        return !(sequenceIndex >= sequence.indexOf(id));
    }

    public int getCurrentSequenceIndex() {
        return (int)engine.getCurrentUserSequenceIndex();
    }
}
