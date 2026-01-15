package core.model.snapshot.chapter;

import java.util.Map;

import core.model.snapshot.Snapshot;

public record ChapterSnapshot(
    String id,
    Map<String, Object> values
) implements Snapshot{

}
