package core.manager.domain.assembler;


import core.model.dto.lesson.LessonPage;
import core.model.snapshot.lesson.LessonPageSnapshot;

public class LessonPageSnapshotAssembler implements ValueSnapshotAssembler<LessonPage, LessonPageSnapshot> {
    private ContentSnapshotAssembler contentSnapshotAssembler;

    public LessonPageSnapshotAssembler(
        ContentSnapshotAssembler contentSnapshotAssembler
    ) {
        this.contentSnapshotAssembler = contentSnapshotAssembler;
    }

    @Override
    public LessonPageSnapshot from(LessonPage object) {
        return new LessonPageSnapshot(
            contentSnapshotAssembler.from(object.contents())
        );
    }

}
