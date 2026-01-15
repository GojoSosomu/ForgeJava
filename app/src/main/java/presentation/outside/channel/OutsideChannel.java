package presentation.outside.channel;

import core.model.view.View;

public interface OutsideChannel<T extends View> {
    void render(T view);
}
