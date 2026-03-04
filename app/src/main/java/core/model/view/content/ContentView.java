package core.model.view.content;

import core.model.dto.content.enums.ContentType;
import core.model.view.View;

public interface ContentView extends View {
    ContentType type();
}
