package presentation.outside.swing.assembler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import core.model.view.chapter.ChapterView;
import presentation.outside.swing.template.SwingChapterCardTemplate;

public final class SwingChapterCardAssembler {

    public List<SwingChapterCardTemplate> assemble(
            List<ChapterView> views
    ) {
        List<SwingChapterCardTemplate> result = new ArrayList<>();

        int x = 40;
        int y = 40;

        for (ChapterView view : views) {
            result.add(new SwingChapterCardTemplate(
                    view.chapterCardView().title(),
                    view.chapterCardView().description(),
                    view.chapterCardView().message(),
                    new Rectangle(x, y, 320, 180)
            ));
        }

        return result;
    }
}

