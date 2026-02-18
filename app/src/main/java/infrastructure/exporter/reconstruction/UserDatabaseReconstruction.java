package infrastructure.exporter.reconstruction;

import java.util.Map;

import core.model.dto.progress.UserDatabaseDTO;

public class UserDatabaseReconstruction implements Reconstruction<Map<String, Object>, UserDatabaseDTO>{
    private UserProgressReconstruction userProgressReconstruction;

    public UserDatabaseReconstruction(
        UserProgressReconstruction userProgressReconstruction
    ) {
        this.userProgressReconstruction = userProgressReconstruction;
    }

    @Override
    public Map<String, Object> reconstruct(UserDatabaseDTO rawData, String id) {
        return Map.of(
            "id", rawData.id(),
            "users", userProgressReconstruction.reconstructList(rawData.users())
        );
    }  
}
