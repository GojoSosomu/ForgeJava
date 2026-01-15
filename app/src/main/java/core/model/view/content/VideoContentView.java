package core.model.view.content;

import core.model.dto.content.ContentType;

public record VideoContentView(
    String videoUrl
) implements ContentView{

    @Override
    public ContentType type() {
        return ContentType.VIDEO;
    }

}
