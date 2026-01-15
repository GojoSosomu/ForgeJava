package core.model.dto.chapter;

import java.util.List;

import core.model.dto.content.TextContent;

public record ChapterIntro(
    String title,
    List<TextContent> description,
    List<TextContent> objectives
) {
    
}
