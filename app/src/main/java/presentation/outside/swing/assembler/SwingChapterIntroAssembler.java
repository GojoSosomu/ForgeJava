package presentation.outside.swing.assembler;

import java.util.ArrayList;
import java.util.List;

import core.model.view.chapter.ChapterView;
import presentation.outside.swing.template.SwingChapterIntroTemplate;

public class SwingChapterIntroAssembler {

    public List<SwingChapterIntroTemplate> assembleIntroTemplates(List<ChapterView> views) {
        List<SwingChapterIntroTemplate> result = new ArrayList<>();
        
        for (ChapterView view : views) {
            result.add(new SwingChapterIntroTemplate(
                view
            ));
        }

        return result;
    }
}
