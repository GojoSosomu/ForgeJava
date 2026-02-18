package core.manager.saver.order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import core.manager.saver.SaveType;
import core.manager.saver.SavingRequest;

public class DefaultSavingOrder implements SavingOrder {

    @Override
    public List<SavingRequest> orders() {
        String userProgressPath;
        try {
            Path appDir = Paths.get(System.getProperty("user.dir")); 
            
            Path storageDir = appDir.resolve("user_datas");

            if (Files.notExists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            userProgressPath = storageDir.resolve("user_progress.json")
                                         .toAbsolutePath()
                                         .toString();

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }

        return List.of(
            new SavingRequest("User Progress", userProgressPath, SaveType.USER_DATABASE)
        );
    }
}
