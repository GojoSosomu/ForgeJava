package core.model.view.chapter;

import core.model.view.View;
import core.model.view.content.TextContentView;

public record ChapterCardView(
    String title,
    String description,
    TextContentView message
) implements View {

}
