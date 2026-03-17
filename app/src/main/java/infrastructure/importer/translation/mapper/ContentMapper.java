package infrastructure.importer.translation.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.content.*;
import core.model.dto.content.enums.ContentType;
import infrastructure.importer.translation.maker.content.*;

public final class ContentMapper implements Mapper<Map<String, Object>, Content> {
    private Map<ContentType, ContentMaker> contentTypeMap;

    public ContentMapper(Map<ContentType, ContentMaker> contentTypeMap) {
        this.contentTypeMap = contentTypeMap;
    }

    @Override
    public Content single(Map<String, Object> raw) {
        if (raw == null || raw.isEmpty()) return null;
        
        String typeKey = (String)raw.get("type");
        if (typeKey == null) throw new IllegalArgumentException(
            "Forgot explicit the type of content, Please check the external database"
        );

        ContentType type = ContentType.fromString(typeKey);

        return contentTypeMap.get(type).make(raw);
    }

    @Override
    public List<Content> list(List<Map<String, Object>> rawList) {
        if (rawList == null) return List.of();

        List<Content> contents = new ArrayList<>();

        for(Map<String, Object> content : rawList) {
            contents.add(this.single(content));
        }

        return contents;
    }

    public Map<String, Content> map(Map<String, Map<String, Object>> rawMap) {
        if (rawMap == null) return Map.of();

        Map<String, Content> contents = new HashMap<>();

        for(Map.Entry<String, Map<String, Object>> content : rawMap.entrySet()) {
            contents.put(content.getKey(), this.single(content.getValue()));
        }

        return contents;
    }
}
