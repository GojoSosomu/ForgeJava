package presentation.service;

import core.engine.Engine;
import core.engine.lifecycle.loader.LoaderExecutor;
import core.engine.lifecycle.loader.LoaderSetUp;
import infrastructure.event.receiver.LoadingReceiver;

public class BootService extends AService {

    public BootService(
        Engine engine
    ) {
        super(engine);
    }
    public void boot(
        LoadingReceiver loadingReceiver
    ) {        
        LoaderExecutor executor = LoaderSetUp.create(engine, loadingReceiver);
        executor.execute();
        engine.initialize();
    }
    
    public Engine getEngine() {
        return engine;
    }
}
