package core.model.dto.content;

public record TextContent(
    String text
) implements Content {
    
    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }
}
