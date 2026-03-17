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
    
    // NEW: The Page Counter Label
    private final JLabel pageCounterLabel;

    public SwingLessonPanel(
        LessonView lessonView, 
        SwingLauncher launcher, 
        Runnable onFinish
    ) {
        this.launcher = launcher;
        this.onFinish = onFinish;

        this.carousel = new CarouselUtility<>(lessonView.pages());
        this.carousel.setWrapAround(false);

        setLayout(new BorderLayout());
        setBackground(PAGE_BASE);
        setFocusable(true);
        setUpGestureListener();

        // 1. Create a Header for the Counter
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        header.setOpaque(false);
        
        pageCounterLabel = new JLabel();
        pageCounterLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        pageCounterLabel.setForeground(withAlpha(INK_MEDIUM, 180)); // Subtle but readable
        header.add(pageCounterLabel);
        
        add(header, BorderLayout.SOUTH);

        // 2. Setup the "Stage"
        cardLayout = new CardLayout();
        pageContainer = new JPanel(cardLayout);
        pageContainer.setOpaque(false);

        for (int i = 0; i < lessonView.pages().size(); i++) {
            pageContainer.add(createPagePanel(lessonView.pages().get(i)), "PAGE_" + i);
        }

        add(pageContainer, BorderLayout.CENTER);

        updatePageCounter(); // Initial update
    }

    private void updatePageCounter() {
        int current = carousel.getCurrentIndex() + 1;
        int total = carousel.size();
        pageCounterLabel.setText(current + " / " + total);
    }

    private void showNext() {
        if (carousel.moveRight()) {
            cardLayout.show(pageContainer, "PAGE_" + carousel.getCurrentIndex());
            updatePageCounter();
            repaint();
        } else {
            onFinish.run();
        }
    }

    private void showPrevious() {
        if (carousel.moveLeft()) {
            cardLayout.show(pageContainer, "PAGE_" + carousel.getCurrentIndex());
            updatePageCounter();
            repaint();
        }
    }

    private void setUpGestureListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyInput) {
                switch (keyInput.getKeyCode()) {
                    case KeyEvent.VK_D: 
                    case KeyEvent.VK_RIGHT: 
                        showNext(); 
                        break;
                    case KeyEvent.VK_A: 
                    case KeyEvent.VK_LEFT: 
                        showPrevious(); 
                        break;
                    case KeyEvent.VK_ESCAPE:
                        launcher.returnChapterSequence();
                        break;
                }
            }
        });
    }

    private JScrollPane createPagePanel(LessonPageView pageView) {
        JPanel page = new SwingLessonPage(pageView, renderer);
        page.setFocusable(false); // <--- ADD THIS

        JScrollPane sp = new JScrollPane(page);
        sp.setFocusable(false);   // <--- ADD THIS
        sp.getVerticalScrollBar().setFocusable(false); 
        sp.getViewport().setOpaque(false);
        sp.getViewport().setFocusable(false); // Add this one too
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }
    
    public void start() {
        carousel.selectIndex(0);
        cardLayout.show(pageContainer, "PAGE_0");
        updatePageCounter();
        setFocusable(true);
        
        // Crucial: Request focus now that we are definitely attached to the frame
        boolean success = requestFocusInWindow();
        if (!success) {
            requestFocus(); // The "Forceful" way
        }
    }
}