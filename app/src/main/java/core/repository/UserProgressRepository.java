package core.repository;

import core.model.dto.progress.UserProgressDTO;

public class UserProgressRepository extends AbstractRepository<UserProgressDTO> {
    private UserProgressDTO currentUser;

    public void setCurrentUser(UserProgressDTO currentUser) {
        this.currentUser = currentUser;
    }

    public UserProgressDTO getCurrentUser() {
        return new UserProgressDTO(currentUser);
    }
}
