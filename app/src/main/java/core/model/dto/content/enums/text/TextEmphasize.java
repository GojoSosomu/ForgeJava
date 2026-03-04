package core.model.dto.content.enums.text;

public enum TextEmphasize {
    // --- Structural Hierarchy ---
    TITLE,          // Main heading (e.g., Chapter Title)
    SUBTITLE,       // Secondary heading
    LABEL,          // Short descriptors (e.g., "Lesson 1")
    BODY,           // Standard paragraph text
    CAPTION,        // Small, supplementary text (e.g., "LOCKED CONTENT")

    // --- Semantic / Feedback States ---
    SUCCESS,        // Positive feedback or completion
    WARNING,        // Cautionary info
    ERROR,          // Failure or blocked access
    INFO,           // General neutral information (replaced DESCRIPTION)
    
    // --- Emphasis ---
    HIGHLIGHT,      // Attention-grabbing (corrected typo)
    MUTED;          // Faded/de-emphasized text (useful for disabled states)

    public static TextEmphasize fromString(String type) {
        try {
            return TextEmphasize.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Unknown content type: " + type
                + "\nCheck ContentType enum for valid types."
            );
        }
    }
}
