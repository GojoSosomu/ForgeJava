package infrastructure.importer.translation.maker.content;

import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.VideoContent;

public class VideoContentMaker extends ContentMaker {
    @Override
    public VideoContent make(Map<String, Object> raw) {
        return new VideoContent(
            requiredString(raw, "url")
        );
    }

    @Override
    public Content make(Map<String, Object> raw, String id) {
        return null;
    }
}
