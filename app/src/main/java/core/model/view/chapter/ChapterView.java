package core.model.view.chapter;

import core.model.view.View;

public record ChapterView(
    String id,
    ChapterCardView chapterCardView,
    ChapterIntroView chapterIntroView,
    ChapterSequenceView chapterSequenceView,
    ChapterOutroView chapterOutroView
) implements View {

}
