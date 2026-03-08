package presentation.service;

import core.engine.Engine;

public class UserService extends AService {

    public UserService(Engine engine) {
        super(engine);
    }

    public void completedItem(String id) {
        engine.updatedProgress(id);
    }
}
