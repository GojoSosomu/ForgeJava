package core.repository;

import java.util.List;

import core.model.dto.progress.UserDatabaseDTO;
import core.model.dto.progress.UserProgressDTO;

public class UserProgressRepository extends AbstractRepository<UserProgressDTO> {
    private String currentUser = "";

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        System.out.println("Setting current user to: " + this.currentUser);
    }

    public UserProgressDTO getCurrentUser() {
        return this.get(currentUser);
    }

    public String getCurrentUsername() {
        return this.currentUser;
    }

    public UserDatabaseDTO getUserDatabase() {
        return new UserDatabaseDTO(
            List.copyOf(getAll().values()), 
            currentUser
        );
    }
}
