package presentation.service;

import core.engine.Engine;
import core.engine.lifecycle.loader.LoaderExecutor;
import core.engine.lifecycle.loader.LoaderSetUp;
import core.engine.lifecycle.saver.SaverSetUp;
import core.engine.lifecycle.saver.SaverExecutor;
import infrastructure.event.receiver.LoadingReceiver;

public class BootService extends AService {
    SaverExecutor saverExecutor;

    public BootService(
        Engine engine
    ) {
        super(engine);
    }

    public void boot(
        LoadingReceiver loadingReceiver
    ) {        
        LoaderExecutor executor = LoaderSetUp.create(engine, loadingReceiver);
        saverExecutor = SaverSetUp.create(engine);
        executor.execute();
        engine.initialize();
    }

    public void unboot() {
        saverExecutor.execute();
    }
    
    public Engine getEngine() {
        return engine;
    }
}
