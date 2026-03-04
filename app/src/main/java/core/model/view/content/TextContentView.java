package core.model.view.content;

import core.model.dto.content.enums.ContentType;
import core.model.dto.content.enums.text.TextStyle;

public record TextContentView(
    String text,
    TextStyle style
) implements ContentView{

    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }
    
}
