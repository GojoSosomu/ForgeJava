package core.model.dto.content;

import core.model.dto.content.enums.ContentType;
import core.model.dto.content.enums.text.TextStyle;

public record TextContent(
    String text,
    TextStyle style
) implements Content {
    
    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }
}
