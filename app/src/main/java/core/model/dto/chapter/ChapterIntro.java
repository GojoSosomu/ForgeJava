package core.model.dto.chapter;

import java.util.List;

import core.model.dto.content.TextContent;

public record ChapterIntro(
    TextContent title,
    TextContent description,
    List<TextContent> objectives
) {
    
}
