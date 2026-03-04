package core.model.dto.chapter;

import java.util.List;

import core.model.dto.content.TextContent;

public record ChapterIntro(
    TextContent title,
    List<TextContent> description,
    List<TextContent> objectives
) {
    
}
