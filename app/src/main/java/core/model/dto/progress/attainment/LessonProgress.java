package core.model.dto.progress.attainment;

import java.util.List;

public record LessonProgress (
    List<String> completedLessons
) {

}
