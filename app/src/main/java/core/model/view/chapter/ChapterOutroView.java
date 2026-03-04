package core.model.view.chapter;

import java.util.List;

import core.model.view.View;
import core.model.view.content.ContentView;
import core.model.view.content.TextContentView;

public record ChapterOutroView(
    TextContentView title,
    TextContentView description,
    List<ContentView> sneakPeaks,
    List<ContentView> conclusion
) implements View {

}
