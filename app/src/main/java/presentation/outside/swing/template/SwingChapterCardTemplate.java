package presentation.outside.swing.template;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import core.model.view.chapter.ChapterView;
import core.model.view.content.TextContentView;
import presentation.outside.renderer.SwingRenderer;
import presentation.outside.swing.animation.animator.SwingSlideAnimator;

import static presentation.outside.library.LibraryOfColor.*;

public final class SwingChapterCardTemplate extends JPanel implements SwingSlideAnimator.SlideTarget {

    private TextContentView title;
    private TextContentView description;
    private String id;
    private TextContentView message;
    private boolean isHovered = false;
    private boolean isLocked = false;
    private JComponent parent;
    private SwingRenderer renderer = new SwingRenderer();
    
    private final Rectangle localBounds = new Rectangle(0, 0, 320, 180);

    public SwingChapterCardTemplate(ChapterView view) {
        this.id = view.id();
        this.title = view.chapterCardView().title();
        this.description = view.chapterCardView().description();
        this.message = view.chapterCardView().message();

        this.setPreferredSize(new Dimension(320, 180));
        this.setSize(320, 180);
        this.setOpaque(false);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        localBounds.setBounds(0, 0, width, height);
    }

    public void setParent(JComponent parent) {
        this.parent = parent;
    }

    public void setLocked(boolean locked) {
        if (this.isLocked != locked) {
            this.isLocked = locked;
            triggerRepaint();
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getChapterId() {
        return id;
    }
    
    public boolean hoverTest(Point p) {
        boolean previouslyHovered = this.isHovered;
        this.isHovered = (p != null && localBounds.contains(p));
        
        if (previouslyHovered != this.isHovered) {
            triggerRepaint();
        }
        return previouslyHovered != this.isHovered;
    }

    public boolean hitTest(Point p) {
        return p != null && localBounds.contains(p);
    }

    private void triggerRepaint() {
        this.repaint();
        if (parent != null) {
            parent.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        final int W = getWidth();
        final int H = getHeight();
        final int OFFSET_X = 35; 
        final int OFFSET_Y = 20;
        final int CORNER_ARC = 18;
        final int STRIPE_WIDTH = 5;
        final int STRIPE_X_INSET = 12;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderer.setGraphics2D(g2);

        if (!isLocked) {
            g2.setColor(withAlpha(CARD_SHADOW, isHovered ? 40 : 20));
            g2.fill(new RoundRectangle2D.Double(2, 4, W - 4, H - 4, CORNER_ARC, CORNER_ARC));
        }

        RoundRectangle2D cardShape = new RoundRectangle2D.Double(0, 0, W - 1, H - 1, CORNER_ARC, CORNER_ARC);
        Color topColor = isLocked ? withAdjust(PAGE_BASE, 0.9) : (isHovered ? withAdjust(PAGE_BASE, 1.02) : PAGE_BASE);
        g2.setPaint(new GradientPaint(0, 0, topColor, 0, H, withAdjust(topColor, 0.96)));
        g2.fill(cardShape);

        g2.setColor(isLocked ? LOCKED_GRAY : (isHovered ? ORANGE_BASED : withAdjust(ORANGE_BASED, 0.9)));
        g2.fillRoundRect(STRIPE_X_INSET, OFFSET_Y, STRIPE_WIDTH, H - (OFFSET_Y * 2), 4, 4);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        g2.setColor(isLocked ? withAdjust(INK_DARK, 1.4) : INK_DARK);
        g2.drawString(title.text().toUpperCase(), OFFSET_X, OFFSET_Y + 15);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.setColor(isLocked ? INK_FADED : INK_MEDIUM);
        int nextY = renderer.drawText(description.text(), OFFSET_X, OFFSET_Y + 32, W - OFFSET_X - 20, 0, 0, 0);

        if (!isLocked) {
            int msgY = nextY + 10;
            g2.setColor(withAlpha(isHovered ? ORANGE_BASED : MESSAGE_BASE, isHovered ? 20 : 35));
            g2.fillRoundRect(OFFSET_X - 8, msgY, W - OFFSET_X - 10, 34, 10, 10);
            
            g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
            g2.setColor(isHovered ? withAdjust(ORANGE_BASED, 0.8) : INK_MEDIUM);
            renderer.drawText(message.text(), OFFSET_X, msgY, W - OFFSET_X - 20, 1, 0, 0);
        } else {
            g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 9));
            g2.setColor(LOCKED_GRAY);
            g2.drawString("LOCKED CONTENT", OFFSET_X, H - 20);
        }

        g2.setStroke(new BasicStroke(isHovered ? 1.5f : 0.8f));
        g2.setColor(isLocked ? withAlpha(BORDER_NORMAL, 60) : (isHovered ? ORANGE_BASED : BORDER_NORMAL));
        g2.draw(cardShape);

        if (isLocked) {
            g2.setColor(withAlpha(Color.BLACK, 10));
            g2.fill(cardShape);
        }

        g2.dispose();
    }

    @Override
    public void setOffset(int x, int y) {
    }

    @Override
    public boolean isStillDisplayable() {
        return isDisplayable();
    }
}