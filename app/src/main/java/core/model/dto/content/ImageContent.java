package core.model.dto.content;

public record ImageContent(
    String url
) implements Content {

    @Override
    public ContentType type() {
        return ContentType.IMAGE;
    }
}
