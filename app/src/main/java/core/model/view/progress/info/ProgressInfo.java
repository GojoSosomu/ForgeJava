package core.model.view.progress.info;

import java.util.List;
import java.util.Map;

public record ProgressInfo(
    List<String> completedLessons,
    List<String> completedChapters,
    Map<String, ScoreView> completedActivities,
    byte currentChapter
) {

}
