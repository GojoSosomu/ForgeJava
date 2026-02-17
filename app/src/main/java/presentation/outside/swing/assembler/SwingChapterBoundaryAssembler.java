package presentation.outside.swing.assembler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import core.model.view.chapter.ChapterIntroView;
import core.model.view.content.ContentView;
import core.model.view.content.TextContentView;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.renderer.SwingRenderer;
import presentation.outside.swing.template.SwingChapterIntroTemplate;

public class SwingChapterBoundaryAssembler {

    private SwingRenderer renderer = new SwingRenderer();

    public SwingChapterIntroTemplate assembleIntro(ChapterIntroView introView, SwingAnimationRunner animationRunner) {
        List<JPanel> pages = new ArrayList<>();

        pages.add(makeTitleAndDescription(
            introView.title(),
            introView.description()
        ));

        pages.add(makeListOfText(
            introView.objectives()
        ));

        return new SwingChapterIntroTemplate(
            pages
        );
    }

    /*public SwingChapterBoundaryTemplate assembleOutro(ChapterOutroView outroView, SwingAnimationRunner animationRunner) {
        List<JPanel> pages = new ArrayList<>();

        pages.add(makeTitleAndDescription(
            outroView.title(), 
            outroView.description()
        ));

        pages.add(makeListOfContent(
            outroView.sneakPeaks()
        ));

        pages.add(makeListOfContent(
            outroView.conclusion()
        ));

        return new SwingChapterBoundaryTemplate(
            pages
        );
    }*/

    private JPanel makeListOfContent(List<ContentView> views) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                renderer.setGraphics2D(g2);

                int currentY = 40;
                int padding = 40;
                int width = getWidth();

                        
                for(ContentView view : views) {
                    if(view instanceof TextContentView) currentY = renderer.drawText(((TextContentView) view).text(), 0, currentY, width, 20, 0, padding);
                    else continue;
                }
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    private JPanel makeListOfText(List<TextContentView> objectives) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                renderer.setGraphics2D(g2);

                int currentY = 40;
                int padding = 40;
                int width = getWidth();

                for(TextContentView view : objectives)
                    currentY = renderer.drawText(view.text(), 0, currentY, width, 20, 0, padding);
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    private JPanel makeTitleAndDescription(String title, List<TextContentView> description) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                renderer.setGraphics2D(g2);

                int currentY = 40;
                int padding = 40;
                int width = getWidth();

                currentY = renderer.drawText(title, 0, currentY, width, 10, 0, padding);

                for(TextContentView view : description)
                    currentY = renderer.drawText(view.text(), 0, currentY, width, 20, 0, padding);
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    private JPanel makeTitleAndDescription(String title, String description) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                renderer.setGraphics2D(g2);

                int currentY = 40;
                int padding = 40;
                int width = getWidth();

                currentY = renderer.drawText(title, 0, currentY, width, 10, 0, padding);

                currentY = renderer.drawText(description, 0, currentY, width, 20, 0, padding);
            }
        };

        panel.setOpaque(false);
        return panel;
    }
}
