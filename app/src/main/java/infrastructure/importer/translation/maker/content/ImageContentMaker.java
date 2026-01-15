package infrastructure.importer.translation.maker.content;

import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.ImageContent;

public class ImageContentMaker extends ContentMaker {
    @Override
    public ImageContent make(Map<String, Object> raw) {        
        return new ImageContent(
            requiredString(raw, "url")
        );
    }

    @Override
    public Content make(Map<String, Object> raw, String id) {
        return null;
    }
}