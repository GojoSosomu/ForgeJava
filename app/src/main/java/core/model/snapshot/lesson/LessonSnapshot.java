package core.model.snapshot.lesson;

import java.util.List;

import core.model.snapshot.Snapshot;
import core.model.snapshot.content.ContentSnapshot;

public record LessonSnapshot(
    String id,
    List<ContentSnapshot> contents
) implements Snapshot {

}
