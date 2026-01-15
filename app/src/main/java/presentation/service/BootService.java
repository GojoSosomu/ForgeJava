package presentation.service;

import core.engine.Engine;
import core.engine.lifecycle.EngineSetUp;
import core.engine.lifecycle.loader.LoaderExecutor;
import core.engine.lifecycle.loader.LoaderSetUp;
import infrastructure.event.receiver.LoadingReceiver;

public class BootService {
    private Engine engine;

    public void boot(LoadingReceiver loadingReceiver) {
        engine = EngineSetUp.create();
        LoaderExecutor executor = LoaderSetUp.create(engine, loadingReceiver);
        executor.execute();
        engine.initialize();
    }
    
    public Engine getEngine() {
        return engine;
    }
}
