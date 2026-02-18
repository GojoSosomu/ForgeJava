package presentation.outside.swing.template;

import javax.swing.*;

import core.model.view.chapter.ChapterView;
import core.model.view.content.TextContentView;
import presentation.outside.swing.renderer.SwingRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Set;

import static presentation.outside.library.LibraryOfColor.*;

public final class SwingChapterCardTemplate {

    private Rectangle2D bounds;
    private TextContentView title;
    private TextContentView description;
    private String id;
    private TextContentView message;
    private boolean isHovered = false;
    private boolean isLocked = false;
    private JComponent parent;
    private SwingRenderer renderer = new SwingRenderer();

    public SwingChapterCardTemplate(
            ChapterView view,
            Rectangle2D bounds
    ) {
        this.id = view.id();
        this.title = view.chapterCardView().title();
        this.description = view.chapterCardView().description();
        this.message = view.chapterCardView().message();
        this.bounds = bounds;
    }

    public void setParent(JComponent parent) {
        this.parent = parent;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
        triggerRepaint();
    }

    public void lockedTest(Set<String> availableIds) {
        boolean previouslyLocked = this.isLocked;
        
        this.isLocked = (availableIds == null || !availableIds.contains(this.id));
        
        if (previouslyLocked != this.isLocked) {
            triggerRepaint();
        }
    }

    public boolean hoverTest(Point p) {
        boolean previouslyHovered = this.isHovered;
        this.isHovered = (p != null && bounds.contains(p));
        
        if (previouslyHovered != this.isHovered) {
            triggerRepaint();
        }
        return previouslyHovered != this.isHovered;
    }

    public boolean hitTest(Point p) {
        return p != null && bounds.contains(p);
    }

    private void triggerRepaint() {
        if (parent != null) {
            parent.repaint();
        }
    }

    public void paint(Graphics2D g) {
        final int PADDING = 28;
        final int OFFSET_X = 40; 
        final int OFFSET_Y = 30;
        final int CORNER_ARC = 24;
        final int STRIPE_WIDTH = 6;
        final int STRIPE_X_INSET = 16;
        final int SHADOW_OFFSET_Y = 6;
        final int SHADOW_ALPHA_BASE = 20;
        final int SHADOW_ALPHA_HOVER = 45;
        final int TITLE_BASELINE_Y = 18;
        final int DESC_TOP_MARGIN = 15;
        final int MSG_TOP_MARGIN = 15;
        final int GLASS_INSET = 10;
        final int FOOTER_BOTTOM_MARGIN = 25;

        final int cardX = (int) Math.round(bounds.getX());
        final int cardY = (int) Math.round(bounds.getY());
        final int cardW = (int) Math.round(bounds.getWidth());
        final int cardH = (int) Math.round(bounds.getHeight());

        final int contentWidth = cardW - (PADDING * 2);
        final int textX = cardX + OFFSET_X;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        renderer.setGraphics2D(g2);

        if (!isLocked) {
            g2.setColor(withAlpha(CARD_SHADOW, isHovered ? SHADOW_ALPHA_HOVER : SHADOW_ALPHA_BASE));
            g2.fill(new RoundRectangle2D.Double(cardX + 2, cardY + SHADOW_OFFSET_Y, cardW - 4, cardH, CORNER_ARC, CORNER_ARC));
        }

        RoundRectangle2D cardShape = new RoundRectangle2D.Double(cardX, cardY, cardW, cardH, CORNER_ARC, CORNER_ARC);
        Color topColor = isLocked ? withAdjust(PAGE_BASE, 0.9) : (isHovered ? withAdjust(PAGE_BASE, 1.02) : PAGE_BASE);
        Color bottomColor = withAdjust(topColor, 0.95);
        g2.setPaint(new GradientPaint(cardX, cardY, topColor, cardX, cardY + cardH, bottomColor));
        g2.fill(cardShape);

        int stripeH = cardH - (OFFSET_Y * 2);
        Color stripeColor = isLocked ? LOCKED_GRAY : (isHovered ? ORANGE_BASED : withAdjust(ORANGE_BASED, 0.9));
        g2.setColor(stripeColor);
        g2.fillRoundRect(cardX + STRIPE_X_INSET, cardY + OFFSET_Y, STRIPE_WIDTH, stripeH, 4, 4);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        g2.setColor(isLocked ? withAdjust(INK_DARK, 1.4) : INK_DARK);
        g2.drawString(title.text().toUpperCase(), textX, cardY + OFFSET_Y + TITLE_BASELINE_Y);

        int descY = cardY + OFFSET_Y + TITLE_BASELINE_Y + DESC_TOP_MARGIN;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.setColor(isLocked ? INK_FADED : INK_MEDIUM);
        int nextY = renderer.drawText(description.text(), textX, descY, contentWidth - 20, 0, 0, 0);

        if (!isLocked) {
            int msgY = nextY + MSG_TOP_MARGIN;
            g2.setColor(withAlpha(isHovered ? ORANGE_BASED : MESSAGE_BASE, isHovered ? 25 : 40));
            g2.fillRoundRect(textX - GLASS_INSET, msgY - 5, contentWidth + GLASS_INSET, (cardY + cardH - msgY) - 15, 12, 12);
            
            g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
            g2.setColor(isHovered ? withAdjust(ORANGE_BASED, 0.8) : INK_MEDIUM);
            renderer.drawText(message.text(), textX, msgY, contentWidth - GLASS_INSET, 0, 0, 0);
        } else {
            g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 10));
            g2.setColor(LOCKED_GRAY);
            g2.drawString("SECURED CHAPTER", textX, cardY + cardH - FOOTER_BOTTOM_MARGIN);
        }

        g2.setStroke(new BasicStroke(isHovered ? 1.2f : 0.6f));
        g2.setColor(isLocked ? withAlpha(BORDER_NORMAL, 80) : (isHovered ? ORANGE_BASED : BORDER_NORMAL));
        g2.draw(cardShape);

        if (isLocked) {
            g2.setColor(withAlpha(Color.BLACK, 15));
            g2.fill(cardShape);
        }

        g2.dispose();
    }
}