package presentation.outside.swing;

import javax.swing.*;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.animation.animator.SwingCompositeAnimator;
import presentation.outside.swing.animation.animator.SwingSlideAnimator;
import presentation.outside.swing.animation.animator.easing.LibraryOfEasing;
import presentation.outside.swing.template.SwingChapterCardTemplate;
import presentation.service.ChapterService;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public final class SwingChapterCoveragePanel extends JPanel implements SwingSlideAnimator.SlideTarget {

    private final List<SwingChapterCardTemplate> cards;
    private final ChapterService service;
    private final JButton backButton;
    private final SwingAnimationRunner animationRunner;

    private int currentIndex = 0;
    private int nextIndex = 0;
    private int direction = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    private Point dragStart;
    private boolean isAnimating = false;

    private float indicatorPosition = 0f;
    private final IndicatorTarget indicatorTarget = new IndicatorTarget();

    private Timer holdTimer;
    private static final int HOLD_DELAY = 1000;
    private static final int HOLD_INTERVAL = 70;
    private static final int EXTRA_ANIMATION_DELAY = 50;
    private final int VISUAL_DELAY = 100;
    private final int TOTAL_DURATION = HOLD_INTERVAL + EXTRA_ANIMATION_DELAY + VISUAL_DELAY;
    private static final int DRAG_THRESHOLD = 50;
    private static final int CARD_WIDTH = 600;
    private static final int CARD_HEIGHT = 400;

    private final Color DARK_BLUE_BASE = new Color(15, 25, 45);
    private static final Color DARK_BLUE_DEEP = new Color(7, 10, 18);
    private static final Color JAVA_ORANGE = new Color(249, 115, 22);
    private static final Color SOFT_YELLOW = new Color(253, 224, 71);
    private static final Color TEXT_OFF_WHITE = new Color(248, 250, 252);

    public SwingChapterCoveragePanel(List<SwingChapterCardTemplate> cards, ChapterService service) {
        this.cards = cards;
        this.service = service;
        this.backButton = createBackButton();
        
        this.animationRunner = new SwingAnimationRunner(60);
        this.animationRunner.start();

        setLayout(new BorderLayout());
        setBackground(DARK_BLUE_BASE);
        setFocusable(true);
        setDoubleBuffered(true);

        setupTopBar();
        setupNavigationButtons();
        setupGestureListeners();
        updateCardBounds();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateCardBounds();
            }
        });
    }

    public JButton getBackButton() {
        return backButton;
    }

    @Override
    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
        
        if (isAnimating && x == 0) {
            currentIndex = nextIndex;
            isAnimating = false;
            offsetX = 0;
            direction = 0;
        }
        repaint();
    }

    private class IndicatorTarget implements SwingSlideAnimator.SlideTarget {
        @Override
        public void setOffset(int x, int y) {
            indicatorPosition = x / 1000f; 
            repaint();
        }
        @Override public boolean isStillDisplayable() { return SwingChapterCoveragePanel.this.isStillDisplayable(); }
    }

    @Override
    public boolean isStillDisplayable() {
        return isDisplayable();
    }

    public void navigate(int direction, boolean animate) {
        if (cards.isEmpty() || isAnimating) return;

        int oldIndex = currentIndex;
        this.direction = direction;
        this.nextIndex = (currentIndex + direction + cards.size()) % cards.size();
        
        for (SwingChapterCardTemplate card : cards) {
            card.hoverTest(null);
        }

        if (animate) {
            this.isAnimating = true;

            animationRunner.add(
                new SwingCompositeAnimator(
                    List.of(
                        new SwingSlideAnimator(
                            this, this, 
                            new Point(direction * getWidth(), 0), 
                            new Point(0, 0), 
                            TOTAL_DURATION,
                            LibraryOfEasing.CIRC_OUT
                        ),
                        new SwingSlideAnimator(
                            this, indicatorTarget,
                            new Point(oldIndex * 1000, 0),
                            new Point(nextIndex * 1000, 0), 
                            TOTAL_DURATION,
                            LibraryOfEasing.LINEAR
                        )
                    )
                )
            );
        } else {
            this.currentIndex = nextIndex;
            this.indicatorPosition = currentIndex;
            this.isAnimating = false;
            this.offsetX = 0;
            this.offsetY = 0;
            this.direction = 0;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cards.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        Point2D start = new Point2D.Float(width / 2f, 0);
        Point2D end = new Point2D.Float(width / 2f, height);
        float[] fractions = {0.0f, 1.0f};
        Color[] colors = {DARK_BLUE_BASE, DARK_BLUE_DEEP};

        LinearGradientPaint backgroundGradient = new LinearGradientPaint(start, end, fractions, colors);
        g2.setPaint(backgroundGradient);
        g2.fillRect(0, 0, width, height);

        g2.setPaint(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 35));
        g2.fillRect(0, 0, width, 3);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        AffineTransform original = g2.getTransform();
        int centerX = (getWidth() - CARD_WIDTH) / 2;
        int centerY = (getHeight() - CARD_HEIGHT) / 2;

        if (isAnimating) {
            g2.translate(centerX + offsetX - (direction * getWidth()), centerY);
            cards.get(currentIndex).paint(g2);
            
            g2.setTransform(original);
            g2.translate(centerX + offsetX, centerY);
            cards.get(nextIndex).paint(g2);
        } else {
            g2.translate(centerX + offsetX, centerY + offsetY);
            cards.get(currentIndex).paint(g2);
        }

        g2.setTransform(original);
        drawPageIndicator(g2);
        g2.dispose();
    }

    private void drawPageIndicator(Graphics2D g2) {
        int dotSize = 8;
        int activeSize = 10;
        int spacing = 15;
        int totalWidth = (cards.size() * dotSize) + ((cards.size() - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - 60;

        g2.setColor(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 40));
        for (int i = 0; i < cards.size(); i++) {
            g2.fillOval(startX + (i * (dotSize + spacing)), y, dotSize, dotSize);
        }

        float animX = startX + (indicatorPosition * (dotSize + spacing));
        
        g2.setColor(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 80));
        g2.fillOval((int)animX - 2, y - 2, activeSize + 4, activeSize + 4);
        
        g2.setColor(JAVA_ORANGE);
        g2.fillOval((int)animX - (activeSize-dotSize)/2, y - (activeSize-dotSize)/2, activeSize, activeSize);
    }

    private void updateCardBounds() {
        if (cards.isEmpty()) return;
        Rectangle bounds = new Rectangle(0, 0, CARD_WIDTH, CARD_HEIGHT);
        for (SwingChapterCardTemplate card : cards) {
            card.setBounds(bounds);
            card.setParent(this);
        }
        repaint();
    }

    private void setupGestureListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isAnimating) return;
                dragStart = e.getPoint();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragStart == null || isAnimating) return;
                int deltaX = e.getX() - dragStart.x;
                if (Math.abs(deltaX) > DRAG_THRESHOLD) {
                    navigate(deltaX > 0 ? -1 : 1, true);
                } else {
                    handleCardClick(e.getPoint());
                }
                dragStart = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isAnimating || cards.isEmpty()) return;
                
                int centerX = (getWidth() - CARD_WIDTH) / 2;
                int centerY = (getHeight() - CARD_HEIGHT) / 2;
                Point p = new Point(e.getX() - centerX, e.getY() - centerY);
                
                SwingChapterCardTemplate currentCard = cards.get(currentIndex);
                currentCard.hoverTest(p);
                setCursor(new Cursor(currentCard.hitTest(p) ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!cards.isEmpty() && !isAnimating) {
                    cards.get(currentIndex).hoverTest(null);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> startHold(-1, true);
                    case KeyEvent.VK_RIGHT -> startHold(1, true);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) stopHold();
            }
        };

        addKeyListener(keyAdapter);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void startHold(int direction, boolean animate) {
        if(holdTimer != null && holdTimer.isRunning()) return;
        navigate(direction, animate);
        holdTimer = new Timer(TOTAL_DURATION - VISUAL_DELAY, e -> {
            if(!isAnimating) navigate(direction, animate);
        });
        holdTimer.setInitialDelay(HOLD_DELAY);
        holdTimer.start();
    }

    public void stopHold() {
        if(holdTimer != null) {
            holdTimer.stop();
            holdTimer = null;
        }
    }

    private void setupTopBar() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        topPanel.setOpaque(false);
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void setupNavigationButtons() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        JButton prev = createNavButton("<");
        JButton next = createNavButton(">");
        prev.addActionListener(e -> navigate(-1, true));
        next.addActionListener(e -> navigate(1, true));
        buttonPanel.add(prev, BorderLayout.WEST);
        buttonPanel.add(next, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 40));
        btn.setForeground(new Color(JAVA_ORANGE.getRed(), JAVA_ORANGE.getGreen(), JAVA_ORANGE.getBlue(), 180));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(SOFT_YELLOW); }
            public void mouseExited(MouseEvent e) { 
                btn.setForeground(new Color(JAVA_ORANGE.getRed(), JAVA_ORANGE.getGreen(), JAVA_ORANGE.getBlue(), 180)); 
            }
        });
        return btn;
    }

    private JButton createBackButton() {
        JButton btn = new JButton("← BACK");
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(TEXT_OFF_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(SOFT_YELLOW); }
            public void mouseExited(MouseEvent e) { btn.setForeground(TEXT_OFF_WHITE); }
        });
        return btn;
    }

    private void handleCardClick(Point point) {
        if (cards.isEmpty() || isAnimating) return;
        int centerX = (getWidth() - CARD_WIDTH) / 2;
        int centerY = (getHeight() - CARD_HEIGHT) / 2;
        Point p = new Point(point.x - centerX, point.y - centerY);
        SwingChapterCardTemplate currentCard = cards.get(currentIndex);
        if (currentCard.hitTest(p)) {
            service.onChapterSelected(currentCard.getTitle());
        }
    }
}