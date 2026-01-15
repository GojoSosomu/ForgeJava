package core.model.dto.content;

public enum ContentType {
    TEXT,
    IMAGE,
    VIDEO;

    public static ContentType fromString(String type) {
        try {
            return ContentType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Unknown content type: " + type
                + "\nCheck ContentType enum for valid types."
            );
        }
    }
}
