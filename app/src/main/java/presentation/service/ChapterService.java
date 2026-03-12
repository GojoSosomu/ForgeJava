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
import core.model.snapshot.progress.UserProgressSnapshot;
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

    public int parseId(String id) {
        return Integer.parseInt(id.replaceAll("[^0-9]", ""));
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

        if(engine.getCurrentUser() == null || allIds.size() == 0) return new HashSet<>();
        int limit = Math.min(userProgressAssembler.from(engine.getCurrentUser()).progressInfo().completedChapters().size(), allIds.size() - 1);
        return new HashSet<>(allIds.subList(0, limit));
    }

    public boolean isChapterAvailable(int i) {
        System.out.println((i - 1) + " " + availableChapterId.size());
        return (i - 1) <= availableChapterId.size() && (i - 1) >= 0 ? true : false;
    }

    public ChapterView getChapter(String currentItem) {
        return chapterViewAssembler.from(engine.getChapters().get(currentItem));
    }

    public boolean isItemLocked(String chapterId, String id) {
        short sequenceIndex  = engine.getCurrentUserSequenceIndex();

        ChapterSnapshot snapshot = engine.getChapters().get(chapterId);
        Map<String, Object> value = snapshot.values();
        List<String> sequence = (List<String>)value.get("sequence");

        UserProgressSnapshot userProgressSnapshot = engine.getCurrentUser();
        Map<String, Object> userValue =  userProgressSnapshot.value();
        List<String> completedLessons = (List<String>) ((Map<String, Object>)userValue.get("lessonProgress")).get("completedLessons");
        Set<String> completedActivities = ((Map<String, Object>)((Map<String, Object>)userValue.get("activityProgress")).get("completedActivities")).keySet();
        
        return !(sequenceIndex >= sequence.indexOf(id) || (completedLessons.contains(id) ||  completedActivities.contains(id)));
    }

    public int getCurrentSequenceIndex() {
        return (int)engine.getCurrentUserSequenceIndex();
    }

    public int getCurrentChapterIndex() {
        return engine.getCurrentUserChapterIndex();
    }
}
