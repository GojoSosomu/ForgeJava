package presentation.outside.swing;

import java.awt.Dimension;
import core.model.view.chapter.ChapterView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.page.SwingChapterIntroPanel;
import presentation.outside.swing.template.page.SwingChapterOutroPanel;
import presentation.outside.swing.template.page.SwingChapterSequencePanel;

public class SwingChapterPanel {
    private final SwingLauncher launcher;
    private final ChapterView chapter;
    private final Dimension dimension;

    private SwingChapterIntroPanel introPanel;
    private SwingChapterSequencePanel sequencePanel;
    private SwingChapterOutroPanel outroPanel;

    public SwingChapterPanel(
        ChapterView chapter, 
        Dimension dimension,
        SwingLauncher launcher
    ) {
        this.chapter = chapter;
        this.dimension = dimension;
        this.launcher = launcher;

        Dimension boundaryPageDimension = new Dimension(900, dimension.height);

        this.introPanel = new SwingChapterIntroPanel(chapter.chapterIntroView(), boundaryPageDimension, this::showSequence);
        this.sequencePanel = new SwingChapterSequencePanel(chapter.chapterSequenceView(), launcher::endChapter, this::showOutro);
        this.outroPanel = new SwingChapterOutroPanel(chapter.chapterOutroView(), boundaryPageDimension, launcher::endChapter);
    }

    public void showIntro() { 
        launcher.switchPanel(introPanel, 600, dimension.height);
        introPanel.start();
    }

    public void showSequence() { 
        launcher.switchPanel(sequencePanel, dimension.width, dimension.height);
    }

    public void showOutro() { 
        launcher.switchPanel(outroPanel, 600, dimension.height);
        outroPanel.start();
    }
}