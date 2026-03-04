package core.model.dto.chapter;

import java.util.List;

import core.model.dto.content.Content;
import core.model.dto.content.TextContent;

public record ChapterOutro(
    TextContent title,
    TextContent description,
    List<Content> sneakPeaks,
    List<Content> conclusion
) {
    
}
