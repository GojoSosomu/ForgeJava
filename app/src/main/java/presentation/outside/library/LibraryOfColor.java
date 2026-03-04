package presentation.outside.library;

import java.awt.Color;

public class LibraryOfColor {
    // -- Base Backgrounds --
    public static final Color DARK_BLUE_BASE = new Color(15, 25, 45);
    public static final Color DARK_BLUE_DEEP = new Color(7, 10, 18);
    public static final Color DEEP_SLATE = new Color(30, 35, 50);
    public static final Color RICH_MIDNIGHT = new Color(10, 15, 28); // Deeper than base

    // -- Brand & Accents --
    public static final Color ORANGE_BASED = new Color(249, 115, 22);
    public static final Color ORANGE_HOVER = new Color(251, 146, 60);
    public static final Color GLOW_YELLOW = new Color(241, 196, 15);
    public static final Color SOFT_YELLOW = new Color(253, 224, 71);
    
    // -- Royal & Premium Accents (Great for Headers/Ranks) --
    public static final Color VIBRANT_PURPLE = new Color(139, 92, 246);
    public static final Color AMETHYST_GLOW = new Color(167, 139, 250);
    public static final Color ROYAL_GOLD = new Color(218, 165, 32);

    // -- Cyber/Tech Accents (Great for Progress bars) --
    public static final Color CYAN_NEON = new Color(34, 211, 238);
    public static final Color ELECTRIC_BLUE = new Color(59, 130, 246);
    public static final Color EMERALD_LIGHT = new Color(52, 211, 153);

    // -- Text & Grays --
    public static final Color TEXT_ON_WHITE = new Color(245, 245, 245);
    public static final Color TEXT_OFF_WHITE = new Color(248, 250, 252);
    public static final Color COOL_GRAY = new Color(150, 160, 180);
    public static final Color MUTED_BLUE = new Color(71, 85, 105);
    public static final Color DIM_TEXT = new Color(148, 163, 184); // For small details

    // -- Status & Feedback --
    public static final Color SCORCH_RED = new Color(255, 38, 38);
    public static final Color SOFT_RED = new Color(239, 68, 68, 200);
    public static final Color SUCCESS_GREEN = new Color(34, 197, 94);
    public static final Color WARNING_AMBER = new Color(245, 158, 11);
    public static final Color LOCKED_GRAY = new Color(100, 116, 139, 180);
    
    // -- Glass & Borders --
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color GLASS_WHITE = new Color(255, 255, 255, 30);
    public static final Color GLASS_DARK = new Color(0, 0, 0, 45); // For dark overlays
    public static final Color CARD_SHADOW = new Color(0, 0, 0, 100);
    public static final Color NEON_BORDER = new Color(255, 255, 255, 80);

    // -- Default Color for Covers --
    public static final Color PAGE_BASE = new Color(245, 235, 215);
    public static final Color MESSAGE_BASE = new Color(220, 211, 193);
    public static final Color INK_DARK = new Color(60, 50, 40);
    public static final Color INK_MEDIUM = new Color(90, 75, 60);
    public static final Color INK_FADED = new Color(130, 115, 100);
    public static final Color BORDER_NORMAL = new Color(210, 190, 160);
    public static final Color MESSAGE_BORDER = new Color(126, 114, 96);

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
    }

    public static Color withAdjust(Color c, double factor) {
        int r = (int) Math.max(0, Math.min(255, c.getRed() * factor));
        int g = (int) Math.max(0, Math.min(255, c.getGreen() * factor));
        int b = (int) Math.max(0, Math.min(255, c.getBlue() * factor));
        return new Color(r, g, b, c.getAlpha());
    }

    public static Color blend(Color c1, Color c2, double ratio) {
        float r = (float) Math.max(0.0, Math.min(1.0, ratio));
        float ir = 1.0f - r;

        int red = (int) (c1.getRed() * ir + c2.getRed() * r);
        int green = (int) (c1.getGreen() * ir + c2.getGreen() * r);
        int blue = (int) (c1.getBlue() * ir + c2.getBlue() * r);
        int alpha = (int) (c1.getAlpha() * ir + c2.getAlpha() * r);

        return new Color(red, green, blue, alpha);
    }
}