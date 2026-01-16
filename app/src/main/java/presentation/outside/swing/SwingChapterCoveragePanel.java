package presentation.outside.swing;

import javax.swing.*;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.animation.animator.SwingSlideAnimator;
import presentation.outside.swing.animation.animator.easing.LibraryOfEasing;
import presentation.outside.swing.template.SwingChapterCardTemplate;
import presentation.service.ChapterService;

import java.awt.*;
import java.awt.event.*;
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

    private Timer holdTimer;
    private static final int HOLD_DELAY = 1000;
    private static final int HOLD_INTERVAL = 70;
    private static final int DRAG_THRESHOLD = 50;
    private static final int CARD_WIDTH = 600;
    private static final int CARD_HEIGHT = 400;

    private static final Color DEEP_BLUE = new Color(15, 23, 42);
    private static final Color GLOW_YELLOW = new Color(253, 224, 71);
    private static final Color SOFT_WHITE = new Color(248, 250, 252);

    public SwingChapterCoveragePanel(List<SwingChapterCardTemplate> cards, ChapterService service) {
        this.cards = cards;
        this.service = service;
        this.backButton = createBackButton();
        
        this.animationRunner = new SwingAnimationRunner(60);
        this.animationRunner.start();

        setLayout(new BorderLayout());
        setBackground(DEEP_BLUE);
        setFocusable(true);

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

    @Override
    public boolean isStillDisplayable() {
        return isDisplayable();
    }

    private void updateCardBounds() {
        if (cards.isEmpty()) return;
        int x = (getWidth() - CARD_WIDTH) / 2;
        int y = (getHeight() - CARD_HEIGHT) / 2;
        Rectangle bounds = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
        for (SwingChapterCardTemplate card : cards) {
            card.getBounds().setRect(bounds);
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
                    navigate(deltaX > 0 ? -1 : 1);
                } else {
                    handleCardClick(e.getPoint());
                }
                dragStart = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (cards.isEmpty()) return;
                Point p = new Point(e.getPoint().x - offsetX, e.getPoint().y - offsetY);
                if (cards.get(currentIndex).hoverTest(p)) repaint();
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> startHold(-1);
                    case KeyEvent.VK_RIGHT -> startHold(1);
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

    private void startHold(int direction) {
        if(holdTimer != null && holdTimer.isRunning()) return;

        navigate(direction);

        holdTimer = new Timer(HOLD_INTERVAL, e -> {
            if(!isAnimating) {
                navigate(direction);
            }
        });

        holdTimer.setInitialDelay(HOLD_DELAY);
        holdTimer.start();
    }

    private void stopHold() {
        if(holdTimer != null) {
            holdTimer.stop();
            holdTimer = null;
        }
    }

    private void navigate(int direction) {
        if (cards.isEmpty() || isAnimating) return;
        
        isAnimating = true;
        this.direction = direction;
        nextIndex = (currentIndex + direction + cards.size()) % cards.size();
        
        int startX = -direction * getWidth();
        Point from = new Point(startX, 0);
        Point to = new Point(0, 0);
        
        animationRunner.add(new SwingSlideAnimator(this, this, from, to, 250, LibraryOfEasing.SMOOTH_STEP));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cards.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isAnimating) {
            int currentDrawX = offsetX + (direction * getWidth());

            g2.translate(currentDrawX, 0);
            cards.get(currentIndex).paint(g2);
            g2.translate(-currentDrawX, 0);

            g2.translate(offsetX, 0);
            cards.get(nextIndex).paint(g2);
        } else {
            g2.translate(offsetX, 0);
            cards.get(currentIndex).paint(g2);
        }

        g2.dispose();
        drawPageIndicator(g);
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
        prev.addActionListener(e -> navigate(-1));
        next.addActionListener(e -> navigate(1));
        buttonPanel.add(prev, BorderLayout.WEST);
        buttonPanel.add(next, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 40));
        btn.setForeground(new Color(249, 115, 22, 180));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createBackButton() {
        JButton btn = new JButton("← BACK");
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(SOFT_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void drawPageIndicator(Graphics g) {
        int dotSize = 8;
        int spacing = 12;
        int totalWidth = (cards.size() * dotSize) + ((cards.size() - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - 60;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < cards.size(); i++) {
            g2.setColor(i == (isAnimating ? nextIndex : currentIndex) ? GLOW_YELLOW : new Color(255, 255, 255, 60));
            g2.fillOval(startX + (i * (dotSize + spacing)), y, dotSize, dotSize);
        }
        g2.dispose();
    }

    private void handleCardClick(Point point) {
        if (cards.isEmpty() || isAnimating) return;
        Point p = new Point(point.x - offsetX, point.y - offsetY);
        SwingChapterCardTemplate currentCard = cards.get(currentIndex);
        if (currentCard.hitTest(p)) {
            service.onChapterSelected(currentCard.getTitle());
        }
    }
}