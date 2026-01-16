package presentation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for(Map.Entry<String, ChapterSnapshot> chapter : chapterSnapshots.entrySet()) {
            result.add(chapter.getValue());
        }
        return chapterViewAssembler.from(result);
    }
}
