package core.model.view.chapter;

import core.model.view.View;
import core.model.view.activity.ActivityView;
import core.model.view.lesson.LessonView;

import java.util.List;
import java.util.Map;

public record ChapterSequenceView(
    Map<String, LessonView> lessonViews,
    Map<String, ActivityView> activityViews,
    List<String> sequences
) implements View {

}
