package core.model.view.progress.info;

import java.util.List;

public record ProgressInfo(
    List<String> completedLessons,
    List<String> completedChapters,
    List<String> completedActivities,
    byte currentChapter
) {

}
