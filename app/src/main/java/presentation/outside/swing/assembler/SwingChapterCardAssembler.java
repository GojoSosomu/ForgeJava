package presentation.outside.swing.assembler;

import java.util.ArrayList;
import java.util.List;

import core.model.view.chapter.ChapterView;
import presentation.outside.swing.template.SwingChapterCardTemplate;

public final class SwingChapterCardAssembler {

    public List<SwingChapterCardTemplate> assemble(
            List<ChapterView> views
    ) {
        List<SwingChapterCardTemplate> result = new ArrayList<>();
        
        for (ChapterView view : views) {
            result.add(new SwingChapterCardTemplate(
                view
            ));
        }

        return result;
    }
}

