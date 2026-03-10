package presentation.service;

import core.engine.Engine;

public class UserService extends AService {

    public UserService(Engine engine) {
        super(engine);
    }

    public void completedLessonItem(String id) {
        engine.updatedLessonProgress(id);
    }

    public void completedActivityItem(String id, int score, int total) {
        engine.updatedActivityProgress(id, score, total);
    }
}
