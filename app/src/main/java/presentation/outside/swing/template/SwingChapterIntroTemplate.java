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
    private final List<TextContentView> description;
    private final List<TextContentView> objectives;
    private final SwingRenderer renderer = new SwingRenderer();
    private final JScrollPane scrollPane;

    private static final int CORNER_ARC = 28;
    private static final int STRIPE_WIDTH = 6;
    private static final int LEFT_MARGIN = 65;
    public static final Color GLOW_ORANGE = new Color(255, 140, 50, 40);

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
        setBorder(BorderFactory.createEmptyBorder(30, LEFT_MARGIN, 30, 50));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                renderer.setGraphics2D(g2);

                int y = 25;
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                g2.setColor(INK_DARK);
                g2.drawString(title.text().toUpperCase(), 0, y);

                y += 22;
                g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
                g2.setColor(INK_MEDIUM);
                for (TextContentView desc : description) {
                    y = renderer.drawText(desc.text(), 0, y, getWidth(), 0, 0, 0) + 8;
                }
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() { return new Dimension(100, 120); }
        };
        headerPanel.setOpaque(false);

        ObjectivePanel objContent = new ObjectivePanel();
        scrollPane = new JScrollPane(objContent);
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

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.add(headerPanel, BorderLayout.NORTH);
        centerWrapper.add(scrollPane, BorderLayout.CENTER);

        add(navPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private class ObjectivePanel extends JPanel {
        ObjectivePanel() { setOpaque(false); }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, objectives.size() * 45 + 60);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setGraphics2D(g2);

            g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 10));
            g2.setColor(withAlpha(INK_MEDIUM, 150));
            g2.drawString("LEARNING OBJECTIVES", 0, 15);

            int count = 1;
            int curY = 45;
            for (TextContentView obj : objectives) {
                g2.setColor(GLOW_ORANGE);
                g2.fillOval(0, curY - 14, 20, 20);
                
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.setColor(ORANGE_BASED);
                g2.drawString(String.valueOf(count), count < 10 ? 7 : 4, curY);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.setColor(INK_MEDIUM);
                curY = renderer.drawText(obj.text(), 32, curY, getWidth() - 50, 0, 0, 0) + 25;
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

        g2.setColor(CARD_SHADOW);
        g2.fill(new RoundRectangle2D.Double(4, 6, W - 8, H - 8, CORNER_ARC, CORNER_ARC));

        RoundRectangle2D cardShape = new RoundRectangle2D.Double(0, 0, W - 1, H - 1, CORNER_ARC, CORNER_ARC);
        g2.setPaint(new GradientPaint(0, 0, PAGE_BASE, 0, H, withAdjust(PAGE_BASE, 0.97)));
        g2.fill(cardShape);

        g2.setColor(ORANGE_BASED);
        g2.fillRoundRect(22, 40, STRIPE_WIDTH, H - 85, 4, 4);

        g2.setStroke(new BasicStroke(1.1f));
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