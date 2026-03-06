package presentation.outside.swing.template.page;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import core.model.dto.content.enums.text.*;
import core.model.view.chapter.ChapterIntroView;
import core.model.view.content.TextContentView;
import presentation.outside.renderer.SwingRenderer;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingChapterIntroPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final SwingRenderer renderer = new SwingRenderer();
    private final List<String> sequence = List.of("TITLE", "OBJECTIVES");
    private final CarouselUtility<String> carouselUtility = new CarouselUtility<>(sequence);
    private final Runnable onComplete;
    private final ChapterIntroView view;

    private static final int CORNER_ARC = 18;
    private static final int OFFSET_X = 35;

    public SwingChapterIntroPanel(ChapterIntroView view, Dimension dimension, Runnable onComplete) {
        this.view = view;
        this.onComplete = onComplete;
        this.carouselUtility.setWrapAround(false);

        setLayout(cardLayout);
        setPreferredSize(dimension);
        setOpaque(false);
        setFocusable(true);
        setUpGestureListener();

        JPanel objectivesList = new JPanel() {
            private int calculateInnerWidth() {
                int currentW = getWidth();
                if (currentW <= 0 && getParent() != null) {
                    currentW = getParent().getWidth();
                }
                if (currentW <= 0) {
                    currentW = dimension.width;
                }

                int scrollBarWidth = 12;
                int bulletSpace = 35; 
                
                return Math.max(100, currentW - (OFFSET_X + 40) - scrollBarWidth - bulletSpace);
            }

            @Override
            public Dimension getPreferredSize() {
                if (getGraphics() == null) return new Dimension(100, 500);
                renderer.setGraphics2D((Graphics2D) getGraphics());
                int h = 20;
                int w = calculateInnerWidth();
                for (TextContentView objective : view.objectives()) {
                    h += renderer.measureTextHeight(objective.text(), w) + 15;
                }
                return new Dimension(w, h);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                renderer.setGraphics2D(g2);

                int bulletSize = 6;
                int bulletMargin = 10;
                int contentX = 35;
                int w = calculateInnerWidth();
                int y = 10;

                for (TextContentView objective : view.objectives()) {
                    g2.setColor(ORANGE_BASED);
                    g2.fillOval(bulletMargin, y + 8, bulletSize, bulletSize);
                    y = renderer.drawText(objective, contentX, y, w, 10, 0, 0);
                }
                g2.dispose();
            }
        };
        objectivesList.setOpaque(false);

        add(createStaticPanel(new IntroTitleContent()), sequence.get(0));
        add(createStaticPanel(new IntroObjectivesContent(createScrollablePanel(objectivesList))), sequence.get(1));

        show(sequence.get(0));
    }

    private JPanel createStaticPanel(JPanel content) {
        content.setBorder(BorderFactory.createEmptyBorder(20, OFFSET_X, 20, 40));
        return content;
    }

    private JScrollPane createScrollablePanel(JPanel content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(null);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.getVerticalScrollBar().setUnitIncrement(14);
        return sp;
    }

    private class IntroTitleContent extends JPanel {
        IntroTitleContent() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setGraphics2D(g2);
            int y = renderer.drawText(view.title(), 20, 20, getWidth() - 40, 20, 0, 0);
            renderer.drawText(view.description(), 20, y, getWidth() - 40, 10, 0, 0);
            g2.dispose();
        }
    }

    private class IntroObjectivesContent extends JPanel {
        IntroObjectivesContent(JScrollPane scrollPane) {
            setLayout(new BorderLayout());
            setOpaque(false);
            JPanel header = new JPanel() {
                @Override
                public Dimension getPreferredSize() { return new Dimension(getWidth(), 60); }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    renderer.setGraphics2D(g2);
                    renderer.drawText(new TextContentView("OBJECTIVES", new TextStyle(TextEmphasize.TITLE, TextSize.HUGE)), 0, 10, getWidth(), 10, 0, 0);
                    g2.dispose();
                }
            };
            header.setOpaque(false);
            add(header, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int W = getWidth(), H = getHeight();
        g2.setColor(withAlpha(CARD_SHADOW, 20));
        g2.fill(new RoundRectangle2D.Double(2, 4, W - 4, H - 4, CORNER_ARC, CORNER_ARC));
        RoundRectangle2D cardShape = new RoundRectangle2D.Double(0, 0, W - 1, H - 1, CORNER_ARC, CORNER_ARC);
        g2.setPaint(new GradientPaint(0, 0, PAGE_BASE, 0, H, withAdjust(PAGE_BASE, 0.96)));
        g2.fill(cardShape);
        g2.setStroke(new BasicStroke(0.8f));
        g2.setColor(BORDER_NORMAL);
        g2.draw(cardShape);
        g2.dispose();
    }

    private void show(String cardName) {
        cardLayout.show(this, cardName);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void showNext() {
        SwingUtilities.invokeLater(this::revalidate);
        if (carouselUtility.moveRight()) show(carouselUtility.getCurrentItem());
        else onComplete.run();
    }

    private void showPrevious() {
        if (carouselUtility.moveLeft()) show(carouselUtility.getCurrentItem());
    }

    private void setUpGestureListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT: showPrevious(); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: showNext(); break;
                }
            }
        });
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
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
            g2.dispose();
        }
        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }

    public void start() {
        show(sequence.get(0));
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
}