package core.model.dto.content.enums.text;

public enum TextSize {
    TINY,   // 8px: Metadata, versioning
    SMALL,  // 10px: Captions, "Locked" labels
    MEDIUM, // 16WSpx: Standard body text
    LARGE,  // 20px: Subtitles, card headers
    HUGE;   // 28px: Chapter titles, hero sections

    public static TextSize fromString(String type) {
        try {
            return TextSize.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Unknown content type: " + type
                + "\nCheck ContentType enum for valid types."
            );
        }
    }
}