package core.manager.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.manager.domain.assembler.ContentSnapshotAssembler;
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
    private ContentSnapshotAssembler contentSnapshotAssembler;
    private int i = 0;

    public ChapterManager(
        ChapterRepository chapterRepository,
        LessonManager lessonManager,
        ActivityManager activityManager,
        ContentSnapshotAssembler contentSnapshotAssembler
    ) {
        this.chapterRepository = chapterRepository;
        this.lessonManager = lessonManager;
        this.activityManager = activityManager;
        this.contentSnapshotAssembler = contentSnapshotAssembler;
    }

    public void printAllChapters() {
        chapterRepository.getAll().forEach((id, chapter) -> {
            System.out.println("Chapter ID: " + id);
            System.out.println("Chapter Data: " + chapter +"\n");
        });
    }

    public List<String> findAll() {
        Map<String, ChapterDTO> chapters = chapterRepository.getAll();

        List<String> ids = new ArrayList<>(chapters.keySet());

        return ids;
    }

    public void putDTO(String id, DTO dto) {
        chapterRepository.register(id, (ChapterDTO) dto);
    }

    @Override
    public ChapterSnapshot from(String id) {
        ChapterDTO dto = chapterRepository.get(id);
        List<String> lessonIds = new ArrayList<>();
        List<String> activityIds = new ArrayList<>();

        for(String sequenceId : dto.sequences()) {
            if(lessonManager.isExist(id))
                lessonIds.add(sequenceId);
            else if(activityManager.isExist(id))
                activityIds.add(sequenceId);
        }
        return new ChapterSnapshot(
        dto.id(),
        Map.of(
            "card", Map.of(
                "title",  "Chapter " + ++i + ": " + dto.chapterCard().title(),
                "subTitle", dto.chapterCard().subTitle(),
                "message", contentSnapshotAssembler.from(dto.chapterCard().message())
            ),
            "intro", Map.of(
                "title", dto.chapterIntro().title(),
                "description", contentSnapshotAssembler.from(dto.chapterIntro().description()),
                "objectives", contentSnapshotAssembler.from(dto.chapterIntro().objectives())
            ),
            "sequence", dto.sequences(),
            "lessons", lessonManager.from(lessonIds),
            "activities", activityManager.from(activityIds),
            "outro", Map.of(
                "title", dto.chapterOutro().title(),
                "description", dto.chapterOutro().description(),
                "sneakPeaks", contentSnapshotAssembler.from(dto.chapterOutro().sneakPeaks()),
                "conclusion", contentSnapshotAssembler.from(dto.chapterOutro().conclusion())
            )
        )
       );
    }
}
