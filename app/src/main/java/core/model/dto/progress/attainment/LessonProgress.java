package core.model.dto.progress.attainment;

import java.util.Set;

public record LessonProgress (
    Set<String> completedLessons
) {

}
