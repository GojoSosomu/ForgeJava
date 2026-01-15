package infrastructure.importer.translation.maker.content;

import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.TextContent;

public class TextContentMaker extends ContentMaker {
    @Override
    public TextContent make(Map<String, Object> raw) {
        return new TextContent(
            requiredString(raw, "text")
        );
    }

    @Override
    public Content make(Map<String, Object> raw, String id) {
        return null;
    }
}
