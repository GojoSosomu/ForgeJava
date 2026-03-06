package infrastructure.importer.translation.maker.content;

import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.TextContent;
import core.model.dto.content.enums.text.TextEmphasize;
import core.model.dto.content.enums.text.TextSize;
import core.model.dto.content.enums.text.TextStyle;

public class TextContentMaker extends ContentMaker {
    @Override
    public TextContent make(Map<String, Object> raw) {
        return new TextContent(
            requiredString(raw, "text"),
            makeStyle(raw)
        );
    }

    @Override
    public Content make(Map<String, Object> raw, String id) {
        return null;
    }

    private TextStyle makeStyle(Map<String, Object> raw) {
        Map<String, Object> styleMap = (Map<String, Object>) raw.getOrDefault("style", Map.of());
        return new TextStyle(
            TextEmphasize.fromString((String) styleMap.getOrDefault("emphasis", "BODY")),
            TextSize.fromString((String) styleMap.getOrDefault("size", "MEDIUM"))
        );
    }
}
