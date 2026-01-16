package presentation.service.assembler;

import java.util.ArrayList;
import java.util.List;

import core.model.snapshot.lesson.LessonPageSnapshot;
import core.model.snapshot.lesson.LessonSnapshot;
import core.model.view.lesson.LessonPageView;
import core.model.view.lesson.LessonView;

public class LessonViewAssembler implements ViewAssembler<LessonSnapshot, LessonView> {
    private ContentViewAssembler contentViewAssembler;

    public LessonViewAssembler(
        ContentViewAssembler contentViewAssembler
    ) {
        this.contentViewAssembler = contentViewAssembler;
    }

    @Override
    public LessonView from(LessonSnapshot snapshot) {
        return new LessonView(
            snapshot.id(),
            assemblePageViews(snapshot.pages())
        );
    }

    private List<LessonPageView> assemblePageViews(List<LessonPageSnapshot> pages) {
        List<LessonPageView> result = new ArrayList<>();

        for(LessonPageSnapshot page : pages) {
            result.add(this.assemblePageView(page));
        }

        return result;
    }

    private LessonPageView assemblePageView(LessonPageSnapshot page) {
        return new LessonPageView(
            contentViewAssembler.from(page.contents())
        );
    }
}
