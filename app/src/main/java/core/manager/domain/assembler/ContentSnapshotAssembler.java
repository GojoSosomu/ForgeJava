package core.manager.domain.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import core.model.dto.content.Content;
import core.model.dto.content.ImageContent;
import core.model.dto.content.TextContent;
import core.model.dto.content.VideoContent;
import core.model.snapshot.content.ContentSnapshot;

public class ContentSnapshotAssembler implements ValueSnapshotAssembler<Content, ContentSnapshot> {

    @Override
    public List<ContentSnapshot> from(List<? extends Content> objects) {
        List<ContentSnapshot> result = new ArrayList<>();

        for(Content object : objects) {
            result.add(this.from(object));
        }

        return result;
    }

    @Override
    public ContentSnapshot from(Content object) {
        Map<String, Object> values = new HashMap<>();

        switch(object.type()) {
            case TEXT:
                TextContent text = (TextContent) object;
                values.put("text" , text.text()); 
                break;
            case IMAGE:
                ImageContent image = (ImageContent) object;
                values.put("imageURL", image.url());
                break;
            case VIDEO:
                VideoContent video = (VideoContent) object;
                values.put("videoURL", video.url());
                break;
        }

        return new ContentSnapshot(object.type(), values);
    }

}
