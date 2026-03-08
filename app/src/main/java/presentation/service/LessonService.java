package presentation.service;

import core.engine.Engine;
import core.model.view.lesson.LessonView;
import presentation.service.assembler.LessonViewAssembler;

public class LessonService extends AService {
    private LessonViewAssembler viewAssembler;

    public LessonService(
        Engine engine,
        LessonViewAssembler viewAssembler
    ) {
        super(engine);
        this.viewAssembler = viewAssembler;
    }

    public LessonView getLesson(String id) {
        return viewAssembler.from(engine.getLessons().get(id));
    }
}
