package core.model.dto.progress;

import core.model.dto.DTO;
import core.model.dto.progress.attainment.ActivityProgress;
import core.model.dto.progress.attainment.ChapterProgress;
import core.model.dto.progress.attainment.LessonProgress;
import core.model.dto.progress.attainment.UserAccount;

public record UserProgressDTO(
    String id,
    UserAccount userAccount,
    LessonProgress lessonProgress,
    ChapterProgress chapterProgress,
    ActivityProgress activityProgress,
    int version
) implements DTO{

    public UserProgressDTO updateProgress(
        LessonProgress lessonProgress
    ) {
        return new UserProgressDTO(
            this.id,
            this.userAccount,
            lessonProgress,
            this.chapterProgress,
            this.activityProgress,
            this.version + 1
        );
    }

    public UserProgressDTO updateProgress(
        ChapterProgress chapterProgress
    ) {
        return new UserProgressDTO(
            this.id,
            this.userAccount,
            this.lessonProgress,
            chapterProgress,
            this.activityProgress,
            this.version + 1
        );
    }

    public UserProgressDTO updateProgress(
        ActivityProgress activityProgress
    ) {
        return new UserProgressDTO(
            this.id,
            this.userAccount,
            this.lessonProgress,
            this.chapterProgress,
            activityProgress,
            this.version + 1
        );
    }
}
