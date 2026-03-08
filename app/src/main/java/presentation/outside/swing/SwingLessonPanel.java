package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import core.model.view.lesson.LessonView;
import core.model.view.lesson.LessonPageView;
import presentation.outside.renderer.SwingRenderer;
import presentation.outside.swing.template.page.SwingLessonPage;
import presentation.outside.launcher.SwingLauncher;
import presentation.utility.CarouselUtility;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingLessonPanel extends JPanel {
    private final SwingLauncher launcher;
    private final Runnable onFinish;
    
    private final CarouselUtility<LessonPageView> carousel;
    private final SwingRenderer renderer = new SwingRenderer();
    
    private final JPanel pageContainer;
    private final CardLayout cardLayout;

    public SwingLessonPanel(
        LessonView lessonView, 
        SwingLauncher launcher, 
        Runnable onFinish
    ) {
        this.launcher = launcher;
        this.onFinish = onFinish;

        // 1. Initialize Carousel
        this.carousel = new CarouselUtility<>(lessonView.pages());
        this.carousel.setWrapAround(false);

        setLayout(new BorderLayout());
        setBackground(PAGE_BASE);
        
        // IMPORTANT: Make the panel able to receive Key Events
        setFocusable(true);

        // 2. Setup the "Stage"
        cardLayout = new CardLayout();
        pageContainer = new JPanel(cardLayout);
        pageContainer.setOpaque(false);

        for (int i = 0; i < lessonView.pages().size(); i++) {
            pageContainer.add(createPagePanel(lessonView.pages().get(i)), "PAGE_" + i);
        }

        add(pageContainer, BorderLayout.CENTER);

        // 3. Setup the Key Listener (The "Invisible Controller")
        setUpGestureListener();
    }

    private void showNext() {
        if (carousel.moveRight()) {
            cardLayout.show(pageContainer, "PAGE_" + carousel.getCurrentIndex());
            repaint();
        } else {
            // "Sensor" reached the end of the line
            onFinish.run();
        }
    }

    private void showPrevious() {
        if (carousel.moveLeft()) {
            cardLayout.show(pageContainer, "PAGE_" + carousel.getCurrentIndex());
            repaint();
        }
    }

    private void setUpGestureListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D: 
                    case KeyEvent.VK_RIGHT: 
                        showNext(); 
                        break;
                        
                    case KeyEvent.VK_A: 
                    case KeyEvent.VK_LEFT: 
                        showPrevious(); 
                        break;
                        
                    case KeyEvent.VK_ESCAPE:
                        launcher.returnChapterSequence(); // Quick exit
                        break;
                }
            }
        });
        
        // Request focus so keys work immediately
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private JScrollPane createPagePanel(LessonPageView pageView) {
        JPanel page = new SwingLessonPage(pageView, renderer);
        page.setOpaque(false);
        JScrollPane sp = new JScrollPane(page);
        sp.setOpaque(false); sp.getViewport().setOpaque(false); sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }
    
    public void start() {
        carousel.selectIndex(0);
        cardLayout.show(pageContainer, "PAGE_0");
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
}
