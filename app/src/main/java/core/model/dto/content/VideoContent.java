package core.model.dto.content;

import core.model.dto.content.enums.ContentType;

public record VideoContent(
    String url
) implements Content {

    @Override
    public ContentType type() {
        return ContentType.VIDEO;
    }
}
