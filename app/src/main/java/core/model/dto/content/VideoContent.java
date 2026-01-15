package core.model.dto.content;

public record VideoContent(
    String url
) implements Content {

    @Override
    public ContentType type() {
        return ContentType.VIDEO;
    }
}
