package core.model.view.progress.info;

import java.util.Set;

public record ProgressInfo(
    Set<String> completedLessons,
    Set<String> completedChapters,
    Set<String> completedActivities
) {

}
