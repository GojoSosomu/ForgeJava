package infrastructure.importer.translation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.enums.ContentType;

public class ContentFilter implements LoadFilter<ContentType, Content, Content> {

    @Override
    public List<Content> listByType(
        List<Content> data, 
        ContentType type
    ) {
        List<Content> result = new ArrayList<>();

        for(Content content : data) {
            if(content.type() == type)
                result.add(content);
        }

        return result;
    }

    @Override
    public Map<String, Content> mapByType(Map<String, Content> data, ContentType type) {
        Map<String, Content> result = new HashMap<>();

        for(Map.Entry<String, Content> content : data.entrySet()) {
            if(content.getValue().type() == type)
                result.put(content.getKey(), content.getValue());
        }

        return result;
    }
}
