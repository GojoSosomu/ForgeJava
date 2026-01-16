package core.model.view.lesson;

import java.util.List;

import core.model.view.View;

public record LessonView(
    String id,
    List<LessonPageView> pages
) implements View {

}
