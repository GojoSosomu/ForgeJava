package core.engine.lifecycle.saver;

import java.util.Map;

import core.engine.Engine;
import core.manager.saver.SaveType;
import core.manager.saver.order.DefaultSavingOrder;
import core.model.dto.progress.UserDatabaseDTO;
import infrastructure.exporter.reconstruction.Reconstruction;
import infrastructure.exporter.reconstruction.UserDatabaseReconstruction;
import infrastructure.exporter.reconstruction.UserProgressReconstruction;
import infrastructure.exporter.writer.*;

public class SaverSetUp {

    public static SaverExecutor create(
        Engine engine
    ) {
        // Initialize the Writers
        Writer jsonWriter = new JsonWriter();

        // Initialize Reconstructions
        UserProgressReconstruction userProgressReconstruction = new UserProgressReconstruction();
        Reconstruction<Map<String, Object>, UserDatabaseDTO> userDatabaseReconstruction = new UserDatabaseReconstruction(userProgressReconstruction);
        // Setup Savers
        Saver<UserDatabaseDTO> userDatabasSaver = new Saver<>(
            jsonWriter,
            userDatabaseReconstruction,
            engine
        );

        Map<SaveType, Saver<?>> savers = Map.of(
            SaveType.USER_DATABASE,
            userDatabasSaver
        );

        // Create and return the SaverExecutor
        SaverExecutor saveExecutor = new SaverExecutor(
            savers,
            new DefaultSavingOrder()
        );

        return saveExecutor;
    }
}
