package core.model.view.content;

import core.model.dto.content.ContentType;

public record TextContentView(
    String text
) implements ContentView{

    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }
    
}
