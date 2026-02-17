package infrastructure.exporter.reconstruction;

import java.util.Map;

import core.model.dto.progress.UserProgressDTO;

public class UserProgressReconstruction implements Reconstruction<Map<String, Object>, UserProgressDTO> {

    @Override
    public Map<String, Object> reconstruct(UserProgressDTO rawData, String id) {
        return Map.of(
            "id", rawData.id(),
            "userAccount", Map.of(
                "password", rawData.userAccount().password(),
                "salt", rawData.userAccount().salt()
            ),
            "lessonProgress", Map.of(
                "completedLessons", rawData.lessonProgress().completedLessons()
            ),
            "chapterProgress", Map.of(
                "completedChapters", rawData.chapterProgress().completedChapters(),
                "currentChapter", rawData.chapterProgress().currentChapter()
            ),
            "activityProgress", Map.of(
                "completedActivities", rawData.activityProgress().completedActivities()
            ),
            "version", rawData.version()
        );
    }

}
