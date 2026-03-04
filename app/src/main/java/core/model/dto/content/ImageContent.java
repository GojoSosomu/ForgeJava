package core.model.dto.content;

import core.model.dto.content.enums.ContentType;

public record ImageContent(
    String url
) implements Content {

    @Override
    public ContentType type() {
        return ContentType.IMAGE;
    }
}
