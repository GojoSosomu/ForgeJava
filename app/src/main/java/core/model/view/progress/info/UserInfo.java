package core.model.view.progress.info;

public record UserInfo(
    String username,
    String passwordHash,
    String salt
) {

}
