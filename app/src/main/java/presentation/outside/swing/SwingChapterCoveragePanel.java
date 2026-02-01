package presentation.outside.swing;

import javax.swing.*;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.animation.animator.*;
import presentation.outside.swing.animation.animator.easing.LibraryOfEasing;
import presentation.outside.swing.template.SwingChapterCardTemplate;
import presentation.service.ChapterService;
import presentation.utility.ChapterCarouselUtility;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

import static presentation.outside.color.LibraryOfColor.*;

public final class SwingChapterCoveragePanel extends JPanel implements SwingSlideAnimator.SlideTarget {

    private final List<SwingChapterCardTemplate> cards;
    private final ChapterService service;
    private final JButton backButton;
    private final SwingAnimationRunner animationRunner;
    private final ChapterCarouselUtility carousel;

    private int animationNextIndex = 0;
    private int animationDirection = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean isAnimating = false;

    private float indicatorPosition = 0f;
    private final IndicatorTarget indicatorTarget = new IndicatorTarget();

    private Timer holdTimer;
    private static final int HOLD_DELAY = 1000;
    private static final int VISUAL_DELAY = 100;
    private static final int EXTRA_ANIMATION_DELAY = 50;
    private final int TOTAL_DURATION = EXTRA_ANIMATION_DELAY + VISUAL_DELAY;
    
    private static final int DRAG_THRESHOLD = 50;
    private static final int CARD_WIDTH = 600;
    private static final int CARD_HEIGHT = 400;
    private Point dragStart;

    public SwingChapterCoveragePanel(List<SwingChapterCardTemplate> cards, ChapterService service) {
        this.cards = cards;
        this.service = service;
        
        List<String> ids = cards.stream().map(SwingChapterCardTemplate::getTitle).collect(Collectors.toList());
        this.carousel = new ChapterCarouselUtility(ids);
        this.indicatorPosition = carousel.getCurrentIndex();

        this.backButton = createTopBackButton();
        this.animationRunner = new SwingAnimationRunner(60);
        this.animationRunner.start();

        setLayout(new BorderLayout());
        setBackground(DARK_BLUE_BASE);
        setFocusable(true);
        setDoubleBuffered(true);

        setupLayout();
        setupGestureListeners();
        updateCardBounds();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateCardBounds();
            }
        });
    }

    @Override
    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
        
        if (isAnimating && x == 0) {
            carousel.selectIndex(animationNextIndex);
            isAnimating = false;
            animationDirection = 0;
        }
        repaint();
    }

    public void navigate(int step, boolean animate) {
        if (cards.isEmpty() || isAnimating || step == 0) return;

        int oldIndex = carousel.getCurrentIndex();
        
        if (step > 0) {
            carousel.moveRight();
            this.animationNextIndex = carousel.getCurrentIndex();
            carousel.selectIndex(oldIndex);
        } else {
            carousel.moveLeft();
            this.animationNextIndex = carousel.getCurrentIndex();
            carousel.selectIndex(oldIndex);
        }

        this.animationDirection = step;
        
        for (SwingChapterCardTemplate card : cards) card.hoverTest(null);

        if (animate) {
            this.isAnimating = true;

            animationRunner.add(
                new SwingCompositeAnimator(
                    List.of(
                        new SwingSlideAnimator(
                            this, this, 
                            new Point(step * getWidth(), 0), 
                            new Point(0, 0), 
                            TOTAL_DURATION,
                            LibraryOfEasing.CIRC_OUT
                        ),
                        new SwingSlideAnimator(
                            this, indicatorTarget,
                            new Point(oldIndex * 1000, 0),
                            new Point(animationNextIndex * 1000, 0), 
                            TOTAL_DURATION,
                            LibraryOfEasing.LINEAR
                        )
                    )
                )
            );
        } else {
            carousel.selectIndex(animationNextIndex);
            this.indicatorPosition = carousel.getCurrentIndex();
            this.offsetX = 0;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cards.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        drawBackground(g2);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        AffineTransform original = g2.getTransform();
        int centerX = (getWidth() - CARD_WIDTH) / 2;
        int centerY = (getHeight() - CARD_HEIGHT) / 2;

        if (isAnimating) {
            g2.translate(centerX + offsetX - (animationDirection * getWidth()), centerY);
            cards.get(carousel.getCurrentIndex()).paint(g2);
            
            g2.setTransform(original);
            g2.translate(centerX + offsetX, centerY);
            cards.get(animationNextIndex).paint(g2);
        } else {
            g2.translate(centerX + offsetX, centerY + offsetY);
            cards.get(carousel.getCurrentIndex()).paint(g2);
        }

        g2.setTransform(original);
        drawPageIndicator(g2);
        g2.dispose();
    }

    private void handleCardClick(Point point) {
        if (cards.isEmpty() || isAnimating) return;
        Point p = getRelativeCardPoint(point);
        if (cards.get(carousel.getCurrentIndex()).hitTest(p)) {
            service.onChapterSelected(carousel.getCurrentChapterId());
        }
    }

    private void handleCardHover(Point point) {
        if (cards.isEmpty() || isAnimating) return;
        Point p = getRelativeCardPoint(point);
        SwingChapterCardTemplate currentCard = cards.get(carousel.getCurrentIndex());
        currentCard.hoverTest(p);
        setCursor(new Cursor(currentCard.hitTest(p) ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    private Point getRelativeCardPoint(Point screenPoint) {
        return new Point(screenPoint.x - (getWidth() - CARD_WIDTH) / 2, 
                         screenPoint.y - (getHeight() - CARD_HEIGHT) / 2);
    }

    private void drawBackground(Graphics2D g2) {
        Point2D start = new Point2D.Float(getWidth() / 2f, 0);
        Point2D end = new Point2D.Float(getWidth() / 2f, getHeight());
        g2.setPaint(new LinearGradientPaint(start, end, new float[]{0f, 1f}, new Color[]{DARK_BLUE_BASE, DARK_BLUE_DEEP}));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 35));
        g2.fillRect(0, 0, getWidth(), 3);
    }

    private void drawPageIndicator(Graphics2D g2) {
        int dotSize = 8, activeSize = 10, spacing = 15;
        int totalWidth = (carousel.size() * dotSize) + ((carousel.size() - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - 60;

        g2.setColor(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 40));
        for (int i = 0; i < carousel.size(); i++) {
            g2.fillOval(startX + (i * (dotSize + spacing)), y, dotSize, dotSize);
        }

        float animX = startX + (indicatorPosition * (dotSize + spacing));
        g2.setColor(ORANGE_BASED);
        g2.fillOval((int)animX - (activeSize-dotSize)/2, y - (activeSize-dotSize)/2, activeSize, activeSize);
    }

    private void setupLayout() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30)); 
        top.setOpaque(false); 
        top.add(backButton); 
        add(top, BorderLayout.NORTH);

        JPanel nav = new JPanel(new BorderLayout()); 
        nav.setOpaque(false);
        JButton prev = createNavBtn("<"); JButton next = createNavBtn(">");
        prev.addActionListener(e -> navigate(-1, true)); 
        next.addActionListener(e -> navigate(1, true));
        nav.add(prev, BorderLayout.WEST); 
        nav.add(next, BorderLayout.EAST); 
        add(nav, BorderLayout.CENTER);
    }

    private JButton createTopBackButton() {
        JButton b = new JButton("← BACK");
        b.setPreferredSize(new Dimension(120, 40));
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(TRANSPARENT);
        b.setForeground(GLOW_YELLOW);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        return b;
    }

    private JButton createNavBtn(String t) {
        JButton b = new JButton(t); 
        b.setPreferredSize(new Dimension(80, 80)); 
        b.setFont(new Font("Segoe UI", Font.BOLD, 48));
        b.setForeground(GLOW_YELLOW); 
        b.setBorderPainted(false); 
        b.setContentAreaFilled(false);
        return b;
    }

    private void updateCardBounds() {
        if (cards.isEmpty()) return;
        Rectangle b = new Rectangle(0, 0, CARD_WIDTH, CARD_HEIGHT);
        for (SwingChapterCardTemplate c : cards) { c.setBounds(b); c.setParent(this); }
    }

    private void setupGestureListeners() {
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { if (!isAnimating) { dragStart = e.getPoint(); requestFocusInWindow(); } }
            @Override public void mouseReleased(MouseEvent e) {
                if (dragStart == null || isAnimating) return;
                int deltaX = e.getX() - dragStart.x;
                if (Math.abs(deltaX) > DRAG_THRESHOLD) navigate(deltaX > 0 ? -1 : 1, true);
                else handleCardClick(e.getPoint());
                dragStart = null;
            }
            @Override public void mouseMoved(MouseEvent e) { handleCardHover(e.getPoint()); }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) startHold(-1);
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) startHold(1);
            }
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) stopHold();
            }
        });
    }

    private void startHold(int dir) {
        if(holdTimer != null && holdTimer.isRunning()) return;
        navigate(dir, true);
        holdTimer = new Timer(TOTAL_DURATION - EXTRA_ANIMATION_DELAY, e -> { if(!isAnimating) navigate(dir, true); });
        holdTimer.setInitialDelay(HOLD_DELAY);
        holdTimer.start();
    }

    private void stopHold() { if(holdTimer != null) holdTimer.stop(); }

    private class IndicatorTarget implements SwingSlideAnimator.SlideTarget {
        @Override public void setOffset(int x, int y) { indicatorPosition = x / 1000f; repaint(); }
        @Override public boolean isStillDisplayable() { return SwingChapterCoveragePanel.this.isStillDisplayable(); }
    }
    @Override public boolean isStillDisplayable() { return isDisplayable(); }
    public JButton getBackButton() { return backButton; }
}