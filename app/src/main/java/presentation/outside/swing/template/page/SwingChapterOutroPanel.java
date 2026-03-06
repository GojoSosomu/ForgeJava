package presentation.outside.swing.template.page;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import core.model.view.content.*;
import core.model.dto.content.enums.text.*;

import core.model.view.chapter.ChapterOutroView;
import presentation.outside.renderer.SwingRenderer;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingChapterOutroPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final SwingRenderer renderer = new SwingRenderer();
    private final List<String> sequence = List.of("TITLE", "SNEAK_PEEK", "CONCLUSION");
    private final CarouselUtility<String> carouselUtility = new CarouselUtility<>(sequence);
    private final Runnable onComplete;
    private final ChapterOutroView view;

    private static final int CORNER_ARC = 18;
    private static final int OFFSET_X = 35;

    public SwingChapterOutroPanel(ChapterOutroView view, Dimension dimension, Runnable onComplete) {
        this.onComplete = onComplete;
        this.view = view;
        this.carouselUtility.setWrapAround(false);

        setLayout(cardLayout);
        setPreferredSize(dimension);
        setOpaque(false);
        setFocusable(true);
        setUpGestureListener();

        JPanel sneakPeekList = new JPanel() {
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
                
                for (ContentView content : view.sneakPeaks()) {
                    h += switch (content.type()) {
                        case TEXT -> renderer.measureTextHeight(((TextContentView) content).text(), w);
                        case IMAGE -> renderer.measureImageHeight(((ImageContentView) content).imageUrl(), w);
                        case VIDEO -> renderer.measureVideoHeight(((VideoContentView) content).videoUrl(), w);
                    } + 10;
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

                for (ContentView content : view.sneakPeaks()) {
                    g2.setColor(ORANGE_BASED);
                    int bulletY = y + 8; 
                    g2.fillOval(bulletMargin, bulletY, bulletSize, bulletSize);

                    y = switch (content.type()) {
                        case TEXT -> renderer.drawText((TextContentView) content, contentX, y, w, 10, 0, 0);
                        case IMAGE -> renderer.drawImage((ImageContentView) content, contentX, y, w, 10, 0, 0);
                        case VIDEO -> renderer.drawVideo((VideoContentView) content, contentX, y, w, 10, 0, 0);
                    }; 
                }
                g2.dispose();
            }
        };
        sneakPeekList.setOpaque(false);

        JPanel conclusionList = new JPanel() {
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
                
                for (ContentView content : view.conclusion()) {
                    h += switch (content.type()) {
                        case TEXT -> renderer.measureTextHeight(((TextContentView) content).text(), w);
                        case IMAGE -> renderer.measureImageHeight(((ImageContentView) content).imageUrl(), w);
                        case VIDEO -> renderer.measureVideoHeight(((VideoContentView) content).videoUrl(), w);
                    } + 10;
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

                for (ContentView content : view.conclusion()) {
                    g2.setColor(ORANGE_BASED);
                    int bulletY = y + 8; 
                    g2.fillOval(bulletMargin, bulletY, bulletSize, bulletSize);

                    y = switch (content.type()) {
                        case TEXT -> renderer.drawText((TextContentView) content, contentX, y, w, 10, 0, 0);
                        case IMAGE -> renderer.drawImage((ImageContentView) content, contentX, y, w, 10, 0, 0);
                        case VIDEO -> renderer.drawVideo((VideoContentView) content, contentX, y, w, 10, 0, 0);
                    }; 
                }
                g2.dispose();
            }
        };
        conclusionList.setOpaque(false);

        add(createStaticPanel(new OutroTitleContent()), sequence.get(0));
        add(createStaticPanel(new OutroSneakPeekContent(createScrollablePanel(sneakPeekList))), sequence.get(1));
        add(createStaticPanel(new OutroConclusionContent(createScrollablePanel(conclusionList))), sequence.get(2));

        SwingUtilities.invokeLater(() -> revalidate());
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
        sp.setBorder(BorderFactory.createEmptyBorder(20, OFFSET_X, 20, 40));
        
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setUnitIncrement(14);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        return sp;
    }

    private class OutroTitleContent extends JPanel {
        OutroTitleContent() { setOpaque(false); }
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

    private class OutroSneakPeekContent extends JPanel {
        OutroSneakPeekContent(JScrollPane scrollPane) {
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel header = new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(getWidth(), 60);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    renderer.setGraphics2D(g2);
                    renderer.applyStyle(new TextStyle(TextEmphasize.TITLE, TextSize.HUGE));
                    renderer.drawText("SNEAK PEEKS", 0, 10, getWidth(), 10, 0, 0);
                    g2.dispose();
                }
            };
            header.setOpaque(false);

            scrollPane.setBorder(null);

            add(header, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private class OutroConclusionContent extends JPanel {
        OutroConclusionContent(JScrollPane scrollPane) {
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel header = new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(getWidth(), 60);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    renderer.setGraphics2D(g2);
                    renderer.applyStyle(new TextStyle(TextEmphasize.TITLE, TextSize.HUGE));
                    renderer.drawText("IN CONCLUSION", 0, 10, getWidth(), 10, 0, 0);
                    g2.dispose();
                }
            };
            header.setOpaque(false);

            scrollPane.setBorder(null);

            add(header, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int W = getWidth();
        int H = getHeight();

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

    private void show(String cardName) {
        cardLayout.show(this, cardName);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void showNext() {
        SwingUtilities.invokeLater(this::revalidate);
        if(carouselUtility.moveRight()) show(carouselUtility.getCurrentItem());
        else onComplete.run();
    }

    private void showPrevious() {
        if(carouselUtility.moveLeft()) show(carouselUtility.getCurrentItem());
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
}