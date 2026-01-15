package core.model.dto.lesson;

import java.util.List;

import core.model.dto.DTO;

public record LessonDTO(
    String id,
    List<LessonPart> pages
) implements DTO {
    
}
