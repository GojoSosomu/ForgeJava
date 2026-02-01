package core.engine.lifecycle.saver;

import java.util.Map;

import core.engine.Engine;
import core.manager.saver.SaveType;
import core.manager.saver.order.DefaultSavingOrder;
import core.model.dto.progress.UserProgressDTO;
import infrastructure.exporter.reconstruction.Reconstruction;
import infrastructure.exporter.reconstruction.UserProgressReconstruction;
import infrastructure.exporter.writer.*;

public class SaverSetUp {

    public static SaverExecutor create(
        Engine engine
    ) {
        // Initialize the Writers
        Writer jsonWriter = new JsonWriter();

        // Initialize Reconstructions
        Reconstruction<Map<String, Object>, UserProgressDTO> userProgressReconstruction = new UserProgressReconstruction();
        
        // Setup Savers
        Saver<UserProgressDTO> userProgressSaver = new Saver<>(
            jsonWriter,
            userProgressReconstruction,
            engine
        );

        Map<SaveType, Saver<?>> savers = Map.of(
            SaveType.USER_PROGRESS,
            userProgressSaver
        );

        // Create and return the SaverExecutor
        SaverExecutor saveExecutor = new SaverExecutor(
            savers,
            new DefaultSavingOrder()
        );

        return saveExecutor;
    }
}
