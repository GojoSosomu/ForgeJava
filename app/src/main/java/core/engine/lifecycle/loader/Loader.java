package core.engine.lifecycle.loader;

import java.util.Map;

import core.engine.Engine;
import core.manager.loader.LoadingRequest;
import core.model.dto.DTO;
import core.model.snapshot.loader.LoadingSnapshot;
import infrastructure.event.pulse.Pulse;
import infrastructure.importer.reader.Reader;
import infrastructure.importer.translation.translator.Translator;

public class Loader<T extends DTO> {
    private Reader reader;
    private Translator<T> translator;
    private Engine engine;

    public Loader(
        Translator<T> translator,
        Reader reader,
        Engine engine
    ) {
        this.reader = reader;
        this.translator = translator;
        this.engine = engine;
    }

    public LoadingSnapshot load(LoadingRequest loadingRequest, Pulse<LoadingSnapshot> loadingReceiver, LoadingSnapshot oldSnapshot) {
        Map<String, Map<String, Object>> rawInfo = reader.read(loadingRequest.path());

        LoadingSnapshot newSnapshot = oldSnapshot.withName(
            loadingRequest.name() + 
            " (" + oldSnapshot.progress() + "/" + oldSnapshot.total() + ")"
        );

        loadingReceiver.onPulse(newSnapshot);
        for(var entry : rawInfo.entrySet()) {
            Map<String, Object> rawData = entry.getValue();
            String id = entry.getKey();
            T dto = translator.translate(rawData, id);
            engine.register(
                loadingRequest.type(),
                id,
                dto
            );
            newSnapshot = newSnapshot.withProgress(newSnapshot.progress() + 1);
            loadingReceiver.onPulse(newSnapshot);
        }

        return newSnapshot;
    }
}
