package core.manager.domain.assembler;


import core.model.dto.lesson.LessonPart;
import core.model.snapshot.lesson.LessonPageSnapshot;

public class LessonPageSnapshotAssembler implements ValueSnapshotAssembler<LessonPart, LessonPageSnapshot> {
    private ContentSnapshotAssembler contentSnapshotAssembler;

    public LessonPageSnapshotAssembler(
        ContentSnapshotAssembler contentSnapshotAssembler
    ) {
        this.contentSnapshotAssembler = contentSnapshotAssembler;
    }

    @Override
    public LessonPageSnapshot from(LessonPart object) {
        return new LessonPageSnapshot(
            contentSnapshotAssembler.from(object.contents())
        );
    }

}
