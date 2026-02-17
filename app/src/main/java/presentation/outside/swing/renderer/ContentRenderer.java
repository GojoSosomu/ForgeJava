package presentation.outside.swing.renderer;

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
}

