package presentation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.model.view.chapter.*;
import core.engine.Engine;
import core.model.snapshot.chapter.*;
import presentation.service.assembler.ChapterViewAssembler;

public class ChapterService extends AService {
    private ChapterViewAssembler chapterViewAssembler;

    public ChapterService(
        ChapterViewAssembler chapterViewAssembler,
        Engine engine
    ) {
        super(engine);
        this.chapterViewAssembler = chapterViewAssembler;
    }

    public void onChapterSelected(String chapterId) {
        System.out.println("Hey, it get pressed on " + chapterId);
    }

    public List<ChapterView> getAllChapters() {
        List<ChapterSnapshot> result = new ArrayList<>();

        Map<String, ChapterSnapshot> chapterSnapshots = engine.getChapters();

        Map<String, ChapterSnapshot> sortedChapterSnapshots = new TreeMap<>(chapterSnapshots);
        for(Map.Entry<String, ChapterSnapshot> chapter : sortedChapterSnapshots.entrySet()) {
            result.add(chapter.getValue());
        }
        
        return chapterViewAssembler.from(result);
    }
}
