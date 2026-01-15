package core.manager.loader;

public record LoadingRequest(
    String name,
    String path,
    LoadType type
) {

}
