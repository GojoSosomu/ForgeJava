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

    public Set<String> availableChapterId() {
        List<String> allIds = getAllChapterID();

        int limit = Math.min(userProgressAssembler.from(engine.getCurrentUser()).progressInfo().currentChapter(), allIds.size());
        return new HashSet<>(allIds.subList(0, limit));
    }

    private boolean checkAvailableChapterId(UserProgressSnapshot info, String id) {
        Set<String> availableChapters = availableChapterId(info);
        return availableChapters.contains(id);
    }

    private Set<String> availableChapterId(UserProgressSnapshot info) {
        List<String> allIds = getAllChapterID();
        
        Map<String, Object> progress = (Map<String, Object>) info.value().get("chapterProgress");
        byte currentChapterCount = ((Number) progress.get("currentChapter")).byteValue();

        int limit = Math.min((int) currentChapterCount, allIds.size());
        return new HashSet<>(allIds.subList(0, limit));
    }
}
