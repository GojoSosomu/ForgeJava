package core.manager.saver;

public record SavingRequest(
    String name,
    String path,
    SaveType type
) {

}
