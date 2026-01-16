package core.manager.domain;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.domain.assembler.LessonPageSnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.lesson.LessonDTO;
import core.model.snapshot.lesson.LessonSnapshot;
import core.repository.LessonRepository;

public class LessonManager implements LoadTarget, EntitySnapshotAssembler<LessonSnapshot> {
    private LessonRepository lessonRepository;
    private LessonPageSnapshotAssembler pageSnapshotAssembler;

    public LessonManager(
        LessonRepository lessonRepository,
        LessonPageSnapshotAssembler pageSnapshotAssembler
    ) {
        this.lessonRepository = lessonRepository;
        this.pageSnapshotAssembler = pageSnapshotAssembler;
    }

    public void printAllLessons() {
        lessonRepository.getAll().forEach((id, lesson) -> {
            System.out.println("Lesson ID: " + id);
            System.out.println("Lesson Data: " + lesson +"\n");
        });
    }

    public void putDTO(String id, DTO dto) {
        lessonRepository.register(id, (LessonDTO)dto);
    }

    public boolean isExist(String id) {
        return lessonRepository.isExist(id);
    }

    @Override
    public LessonSnapshot from(String id) {
        LessonDTO dto = lessonRepository.get(id);
        
        return new LessonSnapshot(
            id,
            pageSnapshotAssembler.from(dto.pages())
        );
    }
}