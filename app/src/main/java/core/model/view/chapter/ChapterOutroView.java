package core.model.view.chapter;

import java.util.List;

import core.model.view.View;
import core.model.view.content.ContentView;

public record ChapterOutroView(
    String title,
    String description,
    List<ContentView> sneakPeaks,
    List<ContentView> conclusion
) implements View {

}
