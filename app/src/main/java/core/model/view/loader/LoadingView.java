package core.model.view.loader;

import core.model.view.View;

public record LoadingView(
    int total,
    int progress,
    String currentTask,
    int percentage
) implements View {

}
