package presentation.service;

import core.engine.Engine;

public abstract class AService {
    protected Engine engine;

    public AService(
        Engine engine
    ) {
        this.engine = engine;
    }
}
