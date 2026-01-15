package infrastructure.importer.translation.filter;

import java.util.ArrayList;
import java.util.List;

import core.model.dto.content.Content;
import core.model.dto.content.ContentType;

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
}
