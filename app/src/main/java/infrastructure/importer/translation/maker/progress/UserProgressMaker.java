package infrastructure.importer.translation.maker.progress;

import java.util.Map;
import java.util.List;

import core.model.dto.progress.UserProgressDTO;
import infrastructure.importer.translation.maker.Maker;
import core.model.dto.progress.attainment.*;

public class UserProgressMaker implements Maker<Map<String, Object>, UserProgressDTO> {
    @Override
    public UserProgressDTO make(Map<String, Object> raw, String id) {
        return new UserProgressDTO(
            (String) raw.get("id"),
            makeUserAccount((Map<String, Object>) raw.get("userAccount")),
            makeLessonProgress((Map<String, Object>) raw.get("lessonProgress")),
            makeChapterProgress((Map<String, Object>) raw.get("chapterProgress")),
            makeActivityProgress((Map<String, Object>) raw.get("activityProgress")),
            (int) raw.get("version")
        );
    }

    private UserAccount makeUserAccount(Map<String, Object> raw) {
        return new UserAccount(
            (String) raw.get("password"),
            (String) raw.get("salt")
        );
    }

    private LessonProgress makeLessonProgress(Map<String, Object> raw) {
        return new LessonProgress(
            (List<String>) raw.get("completedLessons")
        );
    }

    private ChapterProgress makeChapterProgress(Map<String, Object> raw) {
        return new ChapterProgress(
            (List<String>) raw.get("completedChapters"),
            ((Number) raw.get("currentChapter")).byteValue()
        );
    }

    private ActivityProgress makeActivityProgress(Map<String, Object> raw) {
        return new ActivityProgress(
            (List<String>) raw.get("completedActivities")
        );
    }
}
