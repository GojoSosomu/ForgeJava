package presentation.outside.swing.template.page;

import java.awt.*;

import javax.swing.JPanel;

import core.model.view.content.ContentView;
import core.model.view.content.ImageContentView;
import core.model.view.content.TextContentView;
import core.model.view.content.VideoContentView;
import core.model.view.lesson.LessonPageView;
import presentation.outside.renderer.SwingRenderer;

public class SwingLessonPage extends JPanel {
    private SwingRenderer renderer;
    private LessonPageView pageView;

    public SwingLessonPage(
        LessonPageView pageView,
        SwingRenderer renderer
    ) {
        this.pageView = pageView;
        this.renderer = renderer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.setGraphics2D(g2);
        
        int x = 60, y = 40, width = getWidth() - 120;
        for (ContentView content : pageView.contentViews()) {
            y = switch (content.type()) {
                case TEXT -> renderer.drawText((TextContentView) content, x, y, width, 10, 0, 0);
                case IMAGE -> renderer.drawImage((ImageContentView) content, x, y, width, 20, 0, 0);
                case VIDEO -> renderer.drawVideo((VideoContentView) content, x, y, width, 20, 0, 0);
            };
        }
        g2.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() { return new Dimension(800, 1500); }
}
