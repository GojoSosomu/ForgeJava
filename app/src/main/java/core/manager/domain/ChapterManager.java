package core.manager.domain;

import java.util.Map;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.chapter.ChapterDTO;
import core.model.snapshot.chapter.ChapterSnapshot;
import core.repository.ChapterRepository;

public class ChapterManager implements LoadTarget, EntitySnapshotAssembler<ChapterSnapshot> {
    private ChapterRepository chapterRepository;
    private LessonManager lessonManager;
    private ActivityManager activityManager;

    public ChapterManager(
        ChapterRepository chapterRepository,
        LessonManager lessonManager,
        ActivityManager activityManager
    ) {
        this.chapterRepository = chapterRepository;
        this.lessonManager = lessonManager;
        this.activityManager = activityManager;
    }

    public void printAllChapters() {
        chapterRepository.getAll().forEach((id, chapter) -> {
            System.out.println("Chapter ID: " + id);
            System.out.println("Chapter Data: " + chapter +"\n");
        });
    }

    public void putDTO(String id, DTO dto) {
        chapterRepository.register(id, (ChapterDTO) dto);
    }

    @Override
    public ChapterSnapshot from(String id) {
        ChapterDTO dto = chapterRepository.get(id);

       return new ChapterSnapshot(
        dto.id(),
        Map.of(
            "card", dto.chapterCard(),
            "intro", dto.chapterIntro(),
            "sequence", dto.sequences(),
                "lessons", lessonManager.from(id),
                "activities", activityManager.from(id),
            "outro", dto.chapterOutro()
        )
       );
    }
}
