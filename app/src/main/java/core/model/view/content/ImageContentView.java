package core.model.view.content;

import core.model.dto.content.ContentType;

public record ImageContentView(
    String imageUrl
) implements ContentView{

    @Override
    public ContentType type() {
        return ContentType.IMAGE;
    }
}
