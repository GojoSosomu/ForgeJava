package core.model.snapshot.content;

import java.util.Map;

import core.model.dto.content.ContentType;
import core.model.snapshot.Snapshot;

public record ContentSnapshot(
    ContentType type,
    Map<String, Object> values
) implements Snapshot {

}
