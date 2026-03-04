package core.model.dto.chapter;

import core.model.dto.content.TextContent;

public record ChapterCard(
    TextContent title,
    TextContent subTitle,
    TextContent message
) {

}