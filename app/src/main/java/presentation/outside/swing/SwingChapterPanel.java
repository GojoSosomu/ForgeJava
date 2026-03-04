package presentation.outside.swing;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import core.model.view.chapter.ChapterView;
import presentation.outside.swing.template.page.SwingChapterIntroPanel;
import presentation.outside.swing.template.page.SwingChapterOutroPanel;
import presentation.outside.swing.template.page.SwingChapterSequencePanel;

public class SwingChapterPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();

    public SwingChapterPanel(ChapterView chapter, Dimension dimension) {
        setLayout(cardLayout);
        setPreferredSize(dimension);

        add(new SwingChapterIntroPanel(chapter.chapterIntroView(), dimension), "INTRO");
        add(new SwingChapterSequencePanel(chapter.chapterSequenceView()), "SEQUENCE");
        add(new SwingChapterOutroPanel(chapter.chapterOutroView(), dimension), "OUTRO");
        
        showIntro();
    }

    public void showIntro() { cardLayout.show(this, "INTRO"); }
    public void showSequence() { cardLayout.show(this, "SEQUENCE"); }
    public void showOutro() { cardLayout.show(this, "OUTRO"); }
}
