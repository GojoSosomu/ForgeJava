package core.model.dto.chapter;

import core.model.dto.content.TextContent;

public record ChapterCard(
    String title,
    String subTitle,
    TextContent message
) {

}