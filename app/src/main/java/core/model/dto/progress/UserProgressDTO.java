package core.model.dto.progress;

import core.model.dto.progress.attainment.ActivityProgress;
import core.model.dto.progress.attainment.ChapterProgress;
import core.model.dto.progress.attainment.LessonProgress;

public record UserProgressDTO(
    String id,
    LessonProgress lessonProgress,
    ChapterProgress chapterProgress,
    ActivityProgress activityProgress,
    int version
) implements ProgressDTO {

}
