package core.model.dto.chapter;

import java.util.List;

import core.model.dto.DTO;

public record ChapterDTO(
    String id,
    ChapterCard chapterCard,
    ChapterIntro chapterIntro,
    List<String> sequences,
    ChapterOutro chapterOutro
) implements DTO {

}