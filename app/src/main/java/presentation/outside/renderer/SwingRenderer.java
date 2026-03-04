package presentation.outside.renderer;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import core.model.dto.content.enums.text.TextEmphasize;
import core.model.dto.content.enums.text.TextStyle;
import core.model.view.content.ImageContentView;
import core.model.view.content.TextContentView;
import core.model.view.content.VideoContentView;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingRenderer implements ContentRenderer {
    private Graphics2D g2;

    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private List<ClickableRegion> clickableRegions = new ArrayList<>();

    private static class ClickableRegion {
        Rectangle bounds;
        Runnable onClick;

        public ClickableRegion(Rectangle bounds, Runnable onClick) {
            this.bounds = bounds;
            this.onClick = onClick;
        }
    }

    public void setGraphics2D(Graphics2D g2) {
        this.g2 = g2;
    }

    @Override
    public int drawText(TextContentView textContent, int x, int y, float w, int vgap, int hgap, int padding) {
        applyStyle(textContent.style());
        return drawText(textContent.text(), x, y, w, vgap, hgap, padding);
    }

    @Override
    public int drawText(String text, int x, int y, float w, int vgap, int hgap, int padding) {
        if (text == null) return y;

        Graphics2D g = (Graphics2D) g2.create();

        int startX = x + hgap + padding;
        int startY = y + vgap;
        float maxWidth = w - (hgap * 2) - (padding * 2);

        int finalY = drawWrappedText(g, text, startX, startY, maxWidth);

        g.dispose();

        return finalY;
    }

    @Override
    public int drawImage(ImageContentView imageContent, int x, int y, float w, int h, int vgap, int hgap, int padding) {
        return drawImage(imageContent.imageUrl(), x, y, w, h, vgap, hgap, padding);
    }

    @Override
    public int drawImage(String url, int x, int y, float w, int h, int vgap, int hgap, int padding) {
        int drawX = x + hgap + padding;
        int drawY = y + vgap;
        int drawW = (int) w - (hgap * 2) - (padding * 2);
        int drawH = h;

        BufferedImage img = imageCache.get(url);

        if (img == null) {
            try {
                img = ImageIO.read(new File(url));
                imageCache.put(url, img);
            } catch (IOException e) {
                g2.setColor(INK_FADED);
                g2.fillRect(drawX, drawY, drawW, drawH);
                return y + vgap + h + vgap;
            }
        }

        g2.drawImage(img, drawX, drawY, drawW, drawH, null);
        return y + vgap + h + vgap;
    }

    @Override
    public int drawVideo(VideoContentView videoContent, int x, int y, float w, int h, int vgap, int hgap, int padding) {
        return drawVideo(videoContent.videoUrl(), x, y, w, h, vgap, hgap, padding);
    }

    @Override
    public int drawVideo(String url, int x, int y, float w, int h, int vgap, int hgap, int padding) {
        File videoFile = new File(url);
        if(!videoFile.exists()) {
            JOptionPane.showMessageDialog(
                null, 
                "Video file not found: " + videoFile.getAbsolutePath(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return y + vgap + h + vgap;
        } else {
            int textHeight = drawText("> " + videoFile.getName(), x, y, w, vgap, hgap, padding);

            int width = (int) w;
            int height = textHeight - y;

            Rectangle videoRect = new Rectangle(x, y, width, height);
            clickableRegions.add(new ClickableRegion(videoRect, () -> {
                JOptionPane.showMessageDialog(
                    null, 
                    "Playing video: " + videoFile.getAbsolutePath(), 
                    "Video Playback", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                try {
                    Desktop.getDesktop().open(videoFile);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Failed to open video file: " + videoFile.getAbsolutePath(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }));

            return textHeight;
        }
    }

    public void clearClickableRegions() {
        clickableRegions.clear();
    }

    public void handleClick(Point clickPoint) {
        for (ClickableRegion region : clickableRegions) {
            if (region.bounds.contains(clickPoint)) {
                region.onClick.run();
                break;
            }
        }
    }

    public int measureTextHeight(Graphics2D g, String text, int maxWidth) {
        if (text == null || text.isEmpty()) return 0;
        
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split("\\s+");
        String currentLine = "";
        int lineCount = 1; 

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                lineCount++; 
                currentLine = word;
            } else {
                currentLine = testLine;
            }
        }
        
        return lineCount * fm.getHeight();
    }

    @Override
    public void applyStyle(TextStyle style) {
        int size = switch (style.size()) {
            case TINY -> 8;
            case SMALL -> 12;
            case MEDIUM -> 16;
            case LARGE -> 20;
            case HUGE -> 28;
            default -> throw new IllegalArgumentException("Unexpected value: " + style.size());
        };
        g2.setFont(getFontFor(style.emphasis(), size));
        switch (style.emphasis()) {
            case TITLE -> g2.setColor(INK_DARK);
            case SUBTITLE -> g2.setColor(INK_MEDIUM);
            case LABEL -> g2.setColor(ORANGE_BASED);
            case BODY -> g2.setColor(INK_MEDIUM);
            case CAPTION -> g2.setColor(INK_FADED);
            case HIGHLIGHT -> g2.setColor(ORANGE_BASED);
            case MUTED -> g2.setColor(withAlpha(INK_FADED, 100));
            case INFO -> g2.setColor(INK_DARK);
            case SUCCESS -> g2.setColor(SUCCESS_GREEN);
            case WARNING -> g2.setColor(WARNING_AMBER);
            case ERROR -> g2.setColor(SCORCH_RED);
            default -> throw new IllegalArgumentException("Unexpected value: " + style.emphasis());
        }
    }

    private int drawWrappedText(Graphics2D g, String text, int x, int y, float maxWidth) {
        if (text == null || text.isEmpty()) return y;
        
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split("\\s+");
        String currentLine = "";
        int currentY = y + fm.getAscent(); 

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g.drawString(currentLine, x, currentY);
                currentLine = word;
                currentY += fm.getHeight();
            } else {
                currentLine = testLine;
            }
        }
        
        g.drawString(currentLine, x, currentY);
        return currentY + fm.getDescent(); 
    }

    private Font getFontFor(TextEmphasize emphasis, int size) {
        String name = "Segoe UI";
        int style = switch (emphasis) {
            case TITLE, LABEL -> Font.BOLD;
            case CAPTION -> Font.ITALIC;
            default -> Font.PLAIN;
        };
        return new Font(name, style, size);
    }
}
