package core.model.view.chapter;

import java.util.List;

import core.model.view.View;
import core.model.view.content.TextContentView;

public record ChapterIntroView(
    TextContentView title,
    TextContentView description,
    List<TextContentView> objectives
) implements View {

}
