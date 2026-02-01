package presentation.outside.color;

import java.awt.Color;

public class LibraryOfColor {
    public static final Color DARK_BLUE_BASE = new Color(15, 25, 45);
    public static final Color DARK_BLUE_DEEP = new Color(7, 10, 18);
    public static final Color ORANGE_BASED = new Color(249, 115, 22);
    public static final Color GLOW_YELLOW = new Color(241, 196, 15);
    public static final Color SOFT_YELLOW = new Color(253, 224, 71);
    public static final Color TEXT_ON_WHITE = new Color(245, 245, 245);
    public static final Color TEXT_OFF_WHITE = new Color(248, 250, 252);
    public static final Color DEEP_SLATE = new Color(30, 35, 50);
    public static final Color COOL_GRAY = new Color(150, 160, 180);
    public static final Color SCORCH_RED = new Color(220, 38, 38);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static final Color PAGE_BASE = new Color(245, 235, 215);
    public static final Color PAGE_HOVER = new Color(220, 211, 193);
    public static final Color INK_DARK = new Color(60, 50, 40);
    public static final Color INK_MEDIUM = new Color(90, 75, 60);
    public static final Color INK_FADED = new Color(130, 115, 100);
    public static final Color BORDER_NORMAL = new Color(210, 190, 160);
    public static final Color BORDER_HOVER = new Color(126, 114, 96);

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
