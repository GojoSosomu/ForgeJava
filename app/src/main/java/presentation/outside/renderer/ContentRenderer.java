package presentation.outside.renderer;

import core.model.dto.content.enums.text.TextStyle;

import core.model.view.content.*;

public interface ContentRenderer {
    default int drawText(String text, int x, int y, float maxWidth, int hgap, int vgap, int padding) {
        return y;
    }
    default int drawImage(String url, int x, int y, float maxWidth, int maxHeight, int hgap, int vgap, int padding) {
        return y;
    }
    default int drawVideo(String url, int x, int y, float maxWidth, int maxHeight, int hgap, int vgap, int padding) {
        return y;
    }

    int drawText(TextContentView textContent, int x, int y, float w, int vgap, int hgap, int padding);
    int drawImage(ImageContentView imageContent, int x, int y, float w, int h, int vgap, int hgap, int padding);
    int drawVideo(VideoContentView videoContent, int x, int y, float w, int h, int vgap, int hgap, int padding);

    void applyStyle(TextStyle style);
}

