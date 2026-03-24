package presentation.service;

import core.engine.Engine;
import core.model.view.progress.UserProgressView;
import presentation.service.assembler.UserProgressAssembler;

public class UserService extends AService {
    private UserProgressAssembler assembler;

    public UserService(
        UserProgressAssembler assembler,
        Engine engine
    ) {
        super(engine);

        this.assembler = assembler;
    }

    public void completedLessonItem(String id) {
        engine.updatedLessonProgress(id);
    }

    public void completedActivityItem(String id, int score, int total) {
        engine.updatedActivityProgress(id, score, total);
    }

    public void updateScore(String id, int score, int total) {
        engine.updateActivityScore(id, score, total);
    }

    public UserProgressView getCurrentProgressView() {
        return assembler.from(engine.getCurrentUser());
    }

    public void completedChapter(String id) {
        if(!getCurrentProgressView().progressInfo().completedChapters().contains(id))
            engine.updatedChapterProgress(id);
    }
}
