package presentation.outside.swing.template;

import javax.swing.*;

import core.model.view.content.TextContentView;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public final class SwingChapterCardTemplate {

    private Rectangle2D bounds;
    private String title;
    private String description;
    private TextContentView message;
    private boolean isHovered = false;
    private JComponent parent;

    private static final Color PAGE_BASE = new Color(245, 235, 215);
    private static final Color PAGE_HOVER = new Color(220, 211, 193);
    private static final Color INK_DARK = new Color(60, 50, 40);
    private static final Color INK_MEDIUM = new Color(90, 75, 60);
    private static final Color INK_FADED = new Color(130, 115, 100);
    private static final Color BORDER_NORMAL = new Color(210, 190, 160);
    private static final Color BORDER_HOVER = new Color(126, 114, 96);

    public SwingChapterCardTemplate(
            String title,
            String description,
            TextContentView message,
            Rectangle2D bounds
    ) {
        this.title = title;
        this.description = description;
        this.message = message;
        this.bounds = bounds;
    }

    public void setParent(JComponent parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    public boolean hoverTest(Point p) {
        boolean previouslyHovered = this.isHovered;
        this.isHovered = (p != null && bounds.contains(p));
        
        if (previouslyHovered != this.isHovered && parent != null) {
            parent.repaint((int)bounds.getX(), (int)bounds.getY(), 
                           (int)bounds.getWidth(), (int)bounds.getHeight());
        }
        return previouslyHovered != this.isHovered;
    }

    public boolean hitTest(Point p) {
        return p != null && bounds.contains(p);
    }

    public void paint(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        RoundRectangle2D cardShape = new RoundRectangle2D.Double(
                bounds.getX(), bounds.getY(),
                bounds.getWidth(), bounds.getHeight(),
                12, 12
        );

        g.setColor(isHovered ? PAGE_HOVER : PAGE_BASE);
        g.fill(cardShape);

        g.setColor(isHovered ? BORDER_HOVER : BORDER_NORMAL);
        g.setStroke(new BasicStroke(isHovered ? 1.8f : 1.2f));
        g.draw(cardShape);

        g.setFont(new Font("Serif", Font.BOLD, 22));
        g.setColor(INK_DARK);
        g.drawString(title, (float)bounds.getX() + 25, (float)bounds.getY() + 45);

        g.setFont(new Font("Serif", Font.PLAIN, 15));
        g.setColor(INK_MEDIUM);
        int nextY = drawWrappedText(
                g,
                description,
                (int)bounds.getX() + 25,
                (int)bounds.getY() + 75,
                (int)bounds.getWidth() - 50
        );

        g.setFont(new Font("Serif", Font.ITALIC, 14));
        g.setColor(INK_FADED);
        drawWrappedText(
                g,
                message.text(),
                (int)bounds.getX() + 25,
                nextY + 12,
                (int)bounds.getWidth() - 50
        );
    }

    private int drawWrappedText(
            Graphics2D g,
            String text,
            int x,
            int y,
            int maxWidth
    ) {
        if (text == null || text.isEmpty()) return y;
        
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        int currentY = y;

        for (String word : words) {
            String testLine = (line.length() == 0) ? word : line + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word);
                currentY += fm.getHeight();
            } else {
                line = new StringBuilder(testLine);
            }
        }
        g.drawString(line.toString(), x, currentY);
        return currentY + fm.getHeight();
    }
}