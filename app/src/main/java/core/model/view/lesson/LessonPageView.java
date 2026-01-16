package core.model.view.lesson;

import java.util.List;

import core.model.view.content.ContentView;

public record LessonPageView(
    List<ContentView> contentViews
) {

}
