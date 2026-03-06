package presentation.outside.swing.template;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import core.model.view.chapter.ChapterView;
import core.model.view.content.TextContentView;
import presentation.outside.renderer.SwingRenderer;
import presentation.outside.swing.animation.animator.SwingSlideAnimator;

import static presentation.outside.library.LibraryOfColor.*;

public final class SwingChapterIntroTemplate extends JPanel implements SwingSlideAnimator.SlideTarget {

    private final JButton backButton;
    private final JButton startButton;
    private final TextContentView title;
    private final TextContentView description;
    private final List<TextContentView> objectives;
    private final SwingRenderer renderer = new SwingRenderer();
    private final JScrollPane scrollPane;

    private static final int CORNER_ARC = 18; 
    private static final int STRIPE_WIDTH = 5;
    private static final int STRIPE_X_INSET = 12;
    private static final int OFFSET_X = 35; 
    private static final int OFFSET_Y = 10;

    public SwingChapterIntroTemplate(ChapterView view) {
        this.title = view.chapterIntroView().title();
        this.description = view.chapterIntroView().description();
        this.objectives = view.chapterIntroView().objectives();

        this.backButton = createBackButton();
        this.startButton = createStartButton();

        setPreferredSize(new Dimension(800, 500));
        setSize(800, 500);
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        
        setBorder(BorderFactory.createEmptyBorder(20, OFFSET_X, 20, 40));

        // Header now only contains the Title
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                renderer.setGraphics2D(g2);

                g2.setColor(INK_DARK);
                renderer.drawText(title, 0, 0, getWidth(), 5, 0, 0);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                if (getGraphics() == null) return new Dimension(100, 50);
                renderer.setGraphics2D((Graphics2D) getGraphics().create());
                renderer.applyStyle(title.style());
                int h = renderer.measureTextHeight(title.text(), getWidth()) + 5;
                return new Dimension(100, h);
            }
        };
        headerPanel.setOpaque(false);

        // InfoPanel now handles Description + "Learning Objectives" Label + Objectives List
        InfoPanel infoContent = new InfoPanel();
        scrollPane = new JScrollPane(infoContent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navPanel.setOpaque(false);
        navPanel.add(backButton);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        footerPanel.add(startButton, BorderLayout.EAST);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 10));
        centerWrapper.setOpaque(false);
        centerWrapper.add(headerPanel, BorderLayout.NORTH);
        centerWrapper.add(scrollPane, BorderLayout.CENTER);

        add(navPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private class InfoPanel extends JPanel {
        InfoPanel() { 
            setOpaque(false); 
            setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        }

        @Override
        public Dimension getPreferredSize() {
            if (getGraphics() == null) return new Dimension(100, 500);
            renderer.setGraphics2D((Graphics2D) getGraphics());
            
            int totalHeight = 0;
            // 1. Measure Description
            totalHeight += renderer.measureTextHeight(description.text(), getWidth()) + 30;

            // 2. Measure Label space
            totalHeight += 35; 

            // 3. Measure Objectives List
            int vgap = 10;
            for (TextContentView obj : objectives) {
                renderer.applyStyle(obj.style());
                // Measure the text plus the vertical gaps we use in paintComponent
                totalHeight += renderer.measureTextHeight(obj.text(), getWidth() - 50) + vgap + 15;
            }
            
            // 4. ADD THE SAFETY BUFFER
            // This ensures the last item (like #4) has breathing room
            totalHeight += 40; 
            
            return new Dimension(getWidth(), totalHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            renderer.setGraphics2D(g2);

            // 1. Draw Description
            int curY = renderer.drawText(description, 0, 0, getWidth(), 2, 0, 0);
            curY += 25; 

            // 2. Draw "LEARNING OBJECTIVES" Label inside the scrollable area
            g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 10));
            FontMetrics fmLabel = g2.getFontMetrics();
            g2.setColor(withAlpha(INK_MEDIUM, 150));
            g2.drawString("LEARNING OBJECTIVES", 0, curY + fmLabel.getAscent());
            curY += fmLabel.getHeight() + 15;

            // 3. Draw Objectives
            int count = 1;
            for (TextContentView obj : objectives) {
                renderer.applyStyle(obj.style());
                FontMetrics fm = g2.getFontMetrics();
                
                int vgap = 10;
                int firstLineBaseline = curY + fm.getAscent();
                int circleSize = 20;
                int circleY = firstLineBaseline - (fm.getAscent() / 2) - (circleSize / 2) + (circleSize / 4);
                
                g2.setColor(new Color(255, 140, 50, 40));
                g2.fillOval(0, circleY, circleSize, circleSize);
                
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(ORANGE_BASED);
                FontMetrics fmNum = g2.getFontMetrics();
                
                int numX = (circleSize - fmNum.stringWidth(String.valueOf(count))) / 2;
                int numY = circleY + ((circleSize + fmNum.getAscent()) / 2) - 2;
                
                g2.drawString(String.valueOf(count), numX, numY);

                curY = renderer.drawText(obj, 32, curY, getWidth() - 50, vgap, 0, 0);
                count++;
            }
            g2.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int W = getWidth();
        final int H = getHeight();

        g2.setColor(withAlpha(CARD_SHADOW, 20));
        g2.fill(new RoundRectangle2D.Double(2, 4, W - 4, H - 4, CORNER_ARC, CORNER_ARC));

        RoundRectangle2D cardShape = new RoundRectangle2D.Double(0, 0, W - 1, H - 1, CORNER_ARC, CORNER_ARC);
        g2.setPaint(new GradientPaint(0, 0, PAGE_BASE, 0, H, withAdjust(PAGE_BASE, 0.96)));
        g2.fill(cardShape);

        g2.setColor(withAdjust(ORANGE_BASED, 0.9));
        g2.fillRoundRect(STRIPE_X_INSET, OFFSET_Y, STRIPE_WIDTH, H - (OFFSET_Y * 2), 4, 4);

        g2.setStroke(new BasicStroke(0.8f));
        g2.setColor(BORDER_NORMAL);
        g2.draw(cardShape);

        g2.dispose();
    }

    private JButton createBackButton() {
        JButton b = new JButton("← BACK TO DASHBOARD");
        b.setFont(new Font("Segoe UI", Font.BOLD, 10));
        b.setForeground(INK_FADED);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        b.setFocusable(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createStartButton() {
        JButton b = new JButton("START LEARNING") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color baseBtn = blend(PAGE_BASE, INK_DARK, 0.12);
                Color hoverBtn = blend(PAGE_BASE, INK_DARK, 0.18);
                
                g2.setColor(getModel().isRollover() ? hoverBtn : baseBtn);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.setColor(withAlpha(BORDER_NORMAL, 150));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(180, 45));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(INK_DARK);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusable(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() { this.thumbColor = withAlpha(ORANGE_BASED, 80); }
        @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 8, r.y, r.width - 12, r.height, 8, 8);
            g2.dispose();
        }
        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }

    @Override public void setOffset(int x, int y) { setLocation((getParent().getWidth() - getWidth()) / 2, y); }
    @Override public boolean isStillDisplayable() { return isDisplayable(); }
    public JButton getBackButton() { return backButton; }
    public JButton getStartButton() { return startButton; }
}