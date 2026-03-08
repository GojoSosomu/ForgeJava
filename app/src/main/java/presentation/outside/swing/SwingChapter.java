package presentation.outside.swing;

import java.awt.Dimension;
import core.model.view.chapter.ChapterView;
import presentation.outside.launcher.SwingLauncher;
import presentation.outside.swing.template.page.SwingChapterIntroPanel;
import presentation.outside.swing.template.page.SwingChapterOutroPanel;
import presentation.outside.swing.template.page.SwingChapterSequencePanel;
import presentation.service.ChapterService;

public class SwingChapter {
    private final SwingLauncher launcher;
    private final Dimension dimension;

    private SwingChapterIntroPanel introPanel;
    private SwingChapterSequencePanel sequencePanel;
    private SwingChapterOutroPanel outroPanel;

    private ChapterView chapter;

    public SwingChapter(
        ChapterService service,
        ChapterView chapter, 
        Dimension dimension,
        SwingLauncher launcher
    ) {
        this.chapter = chapter;
        this.dimension = dimension;
        this.launcher = launcher;

        Dimension boundaryPageDimension = new Dimension(900, dimension.height);

        this.introPanel = new SwingChapterIntroPanel(chapter.chapterIntroView(), boundaryPageDimension, this::showSequence);
        this.sequencePanel = new SwingChapterSequencePanel(service, launcher, chapter.id(),chapter.chapterSequenceView(), launcher::endChapter, this::showOutro);
        this.outroPanel = new SwingChapterOutroPanel(chapter.chapterOutroView(), boundaryPageDimension, launcher::endChapter);
    }

    public void showIntro() { 
        launcher.switchPanel(introPanel, 600, dimension.height);
        introPanel.start();
    }

    public void showSequence() { 
        launcher.switchPanel(sequencePanel, dimension.width, dimension.height);
        sequencePanel.updated();
    }

    public void showOutro() { 
        launcher.switchPanel(outroPanel, 600, dimension.height);
        outroPanel.start();
    }

    public void updatedSequencePanel(
        ChapterService service,
        ChapterView chapter
    ) {
        this.sequencePanel = new SwingChapterSequencePanel(service, launcher, chapter.id(),chapter.chapterSequenceView(), launcher::endChapter, this::showOutro);
    }

    public String id() {
        return chapter.id();
    }
}