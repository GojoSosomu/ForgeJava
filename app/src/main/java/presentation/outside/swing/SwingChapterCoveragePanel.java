package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.List;

import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.animation.animator.*;
import presentation.outside.swing.animation.animator.easing.LibraryOfEasing;
import presentation.outside.swing.template.SwingChapterCardTemplate;
import presentation.outside.swing.template.SwingChapterIntroTemplate;
import presentation.service.ChapterService;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public final class SwingChapterCoveragePanel extends JPanel implements SwingSlideAnimator.SlideTarget {

    private final List<SwingChapterCardTemplate> cards;
    private final List<SwingChapterIntroTemplate> intros;
    private final ChapterService service;
    private final JButton backButton;
    private final SwingAnimationRunner animationRunner;
    private final CarouselUtility<String> carousel;
    private final SwingLauncher swingLauncher;

    private int animationNextIndex = 0;
    private int animationDirection = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean isCardActive = false;
    private boolean isIntroActive = false;

    private float indicatorPosition = 0f;

    private Timer holdTimer;
    private static final int HOLD_DELAY = 1000;
    private static final int VISUAL_DELAY = 100;
    private static final int EXTRA_ANIMATION_DELAY = 50;
    private final int TOTAL_DURATION = EXTRA_ANIMATION_DELAY + VISUAL_DELAY;
    
    private static final int DRAG_THRESHOLD = 50;
    private static final int CARD_WIDTH = 700; 
    private static final int CARD_HEIGHT = 400;
    private static final int INTRO_W = 800;
    private static final int INTRO_H = 500;
    private Point dragStart;

    public SwingChapterCoveragePanel(
        List<SwingChapterCardTemplate> cards, 
        List<SwingChapterIntroTemplate> intros, 
        ChapterService service, 
        SwingAnimationRunner swingAnimationRunner,
        SwingLauncher swingLauncher
    ) {
        this.cards = cards;
        this.intros = intros;
        this.service = service;
        this.swingLauncher = swingLauncher;
        this.carousel = new CarouselUtility<>(service.getAllChapterID());
        this.indicatorPosition = carousel.getCurrentIndex();
        this.backButton = createTopBackButton();
        this.animationRunner = swingAnimationRunner;
        this.animationRunner.start();

        setLayout(null); 
        setBackground(DARK_BLUE_BASE);
        setFocusable(true);
        setDoubleBuffered(true);

        service.setUpAvailableChapters();
        for (SwingChapterCardTemplate card : cards) {
            card.setVisible(false);
            add(card);
        }

        for (SwingChapterIntroTemplate intro : intros) {
            intro.setVisible(false);
            add(intro);
            setupIntroActions(intro);
        }
        
        if (!cards.isEmpty()) cards.get(0).setVisible(true);

        setupLayout();
        setupGestureListeners();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayouts();
            }
        });
    }

    private void setupIntroActions(SwingChapterIntroTemplate intro) {
        intro.getBackButton().addActionListener(e -> {
            isIntroActive = false;
            int centerX = intro.getX();
            int currentY = intro.getY();
            int exitY = -INTRO_H - 50;

            animationRunner.add(new SwingSlideAnimator(
                this,
                intros.get(carousel.getCurrentIndex()),
                new Point(centerX, currentY),
                new Point(centerX, exitY),
                400, 
                LibraryOfEasing.CIRC_IN
            ));
        });

        intro.getStartButton().addActionListener(e -> {
            isIntroActive = false;
            swingLauncher.startChapter(carousel.getCurrentItem());
        });
    }

    private void popIntro(int index) {
        if (index < 0 || index >= intros.size() || isCardActive || cards.get(index).isLocked()) return;
        
        isIntroActive = true;
        stopHold();

        SwingChapterIntroTemplate intro = intros.get(index);
        int centerX = (getWidth() - INTRO_W) / 2;
        int targetY = (getHeight() - INTRO_H) / 2;
        int startY = -INTRO_H - 50; 

        intro.setSize(INTRO_W, INTRO_H);
        intro.setVisible(true);
        setComponentZOrder(intro, 0);

        animationRunner.add(new SwingSlideAnimator(
            this, 
            intros.get(carousel.getCurrentIndex()),
            new Point(centerX, startY), 
            new Point(centerX, targetY), 
            500, 
            LibraryOfEasing.CIRC_OUT
        ));
    }

    @Override
    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;

        if (isCardActive && getWidth() > 0) {
            float progress = (float) Math.abs(x) / getWidth();
            float lerpFactor = 1.0f - progress;

            int startIdx = carousel.getCurrentIndex();
            int endIdx = animationNextIndex;

            this.indicatorPosition = startIdx + (endIdx - startIdx) * lerpFactor;
        }

        updateCardBounds();

        if (isCardActive && x == 0) {
            carousel.selectIndex(animationNextIndex);
            this.indicatorPosition = (float) carousel.getCurrentIndex(); 
            isCardActive = false;
            animationDirection = 0;
            this.offsetX = 0;
        }
        repaint();
    }

    private void updateLayouts() {
        updateCardBounds();
        for (SwingChapterIntroTemplate intro : intros) {
            if (intro.isVisible()) {
                intro.setLocation((getWidth() - INTRO_W) / 2, (getHeight() - INTRO_H) / 2);
            }
        }
    }

    private void updateCardBounds() {
        if (cards.isEmpty()) return;
        int centerX = (getWidth() - CARD_WIDTH) / 2;
        int centerY = (getHeight() - CARD_HEIGHT) / 2;

        if (isCardActive) {
            cards.get(carousel.getCurrentIndex()).setBounds(centerX + offsetX - (animationDirection * getWidth()), centerY, CARD_WIDTH, CARD_HEIGHT);
            cards.get(animationNextIndex).setBounds(centerX + offsetX, centerY, CARD_WIDTH, CARD_HEIGHT);
        } else {
            cards.get(carousel.getCurrentIndex()).setBounds(centerX + offsetX, centerY + offsetY, CARD_WIDTH, CARD_HEIGHT);
        }
    }

    public void navigate(int step, boolean animate) {
        if (cards.isEmpty() || isCardActive || isIntroActive || step == 0) return;

        int oldIndex = carousel.getCurrentIndex();
        if (step > 0) carousel.moveRight(); else carousel.moveLeft();
        this.animationNextIndex = carousel.getCurrentIndex();
        carousel.selectIndex(oldIndex);
        this.animationDirection = step;
        
        for (SwingChapterCardTemplate card : cards) { card.setVisible(false); }

        if (animate) {
            this.isCardActive = true;
            SwingChapterCardTemplate currentCard = cards.get(oldIndex);
            SwingChapterCardTemplate nextCard = cards.get(animationNextIndex);

            currentCard.setVisible(true);
            nextCard.setVisible(true);
            animationRunner.add(new SwingCompositeAnimator(List.of(
                new SwingSlideAnimator(this, this, new Point(step * getWidth(), 0), new Point(0, 0), TOTAL_DURATION, LibraryOfEasing.CIRC_OUT),
                new SwingSlideAnimator(this, nextCard, new Point(oldIndex * 1000, 0), new Point(animationNextIndex * 1000, 0), TOTAL_DURATION, LibraryOfEasing.LINEAR)
            )));
        } else {
            carousel.selectIndex(animationNextIndex);
            this.indicatorPosition = carousel.getCurrentIndex();
            this.offsetX = 0;
            cards.get(animationNextIndex).setVisible(true);
            updateCardBounds();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        drawBackground(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawPageIndicator(g2);
        g2.dispose();
    }

    public void setUpLocked() {
        for (SwingChapterCardTemplate template : cards) {
            template.setLocked(!service.isChapterAvailable(template.getChapterId()));
        }
    }

    public void resetUI() {
        this.isIntroActive = false;
        this.isCardActive = false;
        this.offsetX = 0;
        this.offsetY = 0;

        updateIntroBounds();
        updateCardBounds();
        repaint();
    }

    private void updateIntroBounds() {
        for (SwingChapterIntroTemplate intro : intros) {
            intro.setVisible(false);
            intro.setLocation((getWidth() - INTRO_W) / 2, -INTRO_H - 100);
        }
    }

    private void setupGestureListeners() {
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { if (!isCardActive && !isIntroActive) { dragStart = e.getPoint(); requestFocusInWindow(); } }
            @Override public void mouseReleased(MouseEvent e) {
                if (dragStart == null || isCardActive || isIntroActive) return;
                int deltaX = e.getX() - dragStart.x;
                if (Math.abs(deltaX) > DRAG_THRESHOLD) navigate(deltaX > 0 ? -1 : 1, true);
                else handleCardClick(e.getPoint());
                dragStart = null;
            }
            @Override public void mouseMoved(MouseEvent e) { if (!isIntroActive) handleCardHover(e.getPoint()); }
        };

        KeyAdapter ka = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if(isIntroActive)
                        intros.get(carousel.getCurrentIndex()).getBackButton().doClick();
                    else 
                        getBackButton().doClick();
                    return;
                }
                if (isCardActive || isIntroActive) return;
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) startHold(-1);
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) startHold(1);
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) handleCardClick(new Point(getWidth() / 2, getHeight() / 2));
            }
            @Override public void keyReleased(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_A || k == KeyEvent.VK_D) stopHold();
            }
        };

        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                resetUI();
                requestFocusInWindow(); 
            }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });

        addKeyListener(ka);
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void startHold(int dir) {
        if (holdTimer != null && holdTimer.isRunning()) return;
        navigate(dir, true);
        holdTimer = new Timer(TOTAL_DURATION, e -> { if (!isCardActive && !isIntroActive) navigate(dir, true); });
        holdTimer.setInitialDelay(HOLD_DELAY);
        holdTimer.start();
    }

    private void stopHold() { if (holdTimer != null) holdTimer.stop(); }

    private void handleCardClick(Point point) {
        if (cards.isEmpty() || isCardActive || isIntroActive) return;
        Point p = getRelativeCardPoint(point);
        if (cards.get(carousel.getCurrentIndex()).hitTest(p)) popIntro(carousel.getCurrentIndex());
    }

    private void handleCardHover(Point point) {
        Point p = getRelativeCardPoint(point);
        SwingChapterCardTemplate currentCard = cards.get(carousel.getCurrentIndex());
        currentCard.hoverTest(p);
        setCursor(new Cursor(currentCard.hitTest(p) ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    private Point getRelativeCardPoint(Point screenPoint) {
        return new Point(screenPoint.x - (getWidth() - CARD_WIDTH) / 2, screenPoint.y - (getHeight() - CARD_HEIGHT) / 2);
    }

    private void drawBackground(Graphics2D g2) {
        Point2D start = new Point2D.Float(getWidth() / 2f, 0);
        Point2D end = new Point2D.Float(getWidth() / 2f, getHeight());
        g2.setPaint(new LinearGradientPaint(start, end, new float[]{0f, 1f}, new Color[]{DARK_BLUE_BASE, DARK_BLUE_DEEP}));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawPageIndicator(Graphics2D g2) {
        int dotSize = 8, activeSize = 10, spacing = 15;
        int totalWidth = (carousel.size() * dotSize) + ((carousel.size() - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - 60;
        g2.setColor(new Color(SOFT_YELLOW.getRed(), SOFT_YELLOW.getGreen(), SOFT_YELLOW.getBlue(), 40));
        for (int i = 0; i < carousel.size(); i++) g2.fillOval(startX + (i * (dotSize + spacing)), y, dotSize, dotSize);
        float animX = startX + (indicatorPosition * (dotSize + spacing));
        g2.setColor(ORANGE_BASED);
        g2.fillOval((int)animX - (activeSize-dotSize)/2, y - (activeSize-dotSize)/2, activeSize, activeSize);
    }

    private void setupLayout() {
        JPanel uiOverlay = new JPanel(new BorderLayout());
        uiOverlay.setOpaque(false);
        uiOverlay.setBounds(0, 0, getWidth(), getHeight());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30)); 
        top.setOpaque(false); 
        top.add(backButton); 
        uiOverlay.add(top, BorderLayout.NORTH);
        JPanel nav = new JPanel(new BorderLayout()); 
        nav.setOpaque(false);
        JButton prevButton = createNavBtn("<"), nextButton = createNavBtn(">");
        prevButton.addActionListener(e -> navigate(-1, true)); 
        nextButton.addActionListener(e -> navigate(1, true));
        nav.add(prevButton, BorderLayout.WEST); 
        nav.add(nextButton, BorderLayout.EAST); 
        uiOverlay.add(nav, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(uiOverlay);
    }

    private JButton createTopBackButton() {
        JButton b = new JButton("← BACK");
        b.setPreferredSize(new Dimension(120, 40));
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setForeground(GLOW_YELLOW);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusable(false);
        return b;
    }

    private JButton createNavBtn(String t) {
        JButton b = new JButton(t); 
        b.setPreferredSize(new Dimension(80, 80)); 
        b.setFont(new Font("Segoe UI", Font.BOLD, 48));
        b.setForeground(GLOW_YELLOW); 
        b.setBorderPainted(false); 
        b.setContentAreaFilled(false);
        b.setFocusable(false);
        return b;
    }

    @Override public boolean isStillDisplayable() { return isDisplayable(); }
    public JButton getBackButton() { return backButton; }
}