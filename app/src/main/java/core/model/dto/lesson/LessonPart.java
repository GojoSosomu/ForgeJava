package core.model.dto.lesson;

import java.util.List;

import core.model.dto.content.Content;

public record LessonPart(
    List<Content> contents
) {

}
