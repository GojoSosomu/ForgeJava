package core.engine.lifecycle.saver;

import infrastructure.exporter.reconstruction.Reconstruction;
import infrastructure.exporter.writer.Writer;

import java.util.Map;

import core.engine.Engine;
import core.manager.saver.SavingRequest;
import core.model.dto.DTO;

public class Saver<T extends DTO> {
    private Writer writer;
    private Reconstruction<Map<String, Object>, T> reconstruction;
    private Engine engine;

    public Saver(
        Writer writer,
        Reconstruction<Map<String, Object>, T> reconstruction,
        Engine engine
    ) {
        this.writer = writer;
        this.reconstruction = reconstruction;
        this.engine = engine;
    }

    public void save(
        SavingRequest savingRequest
    ) {
        Map<String, Map<String, Object>> rawListInfo = reconstruction.reconstructMap(
            engine.getAll(savingRequest.type())
        );

        rawListInfo.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        
        writer.write(savingRequest.path(), rawListInfo);
    }
}
