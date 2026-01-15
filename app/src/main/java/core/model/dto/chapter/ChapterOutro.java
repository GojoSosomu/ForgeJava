package core.model.dto.chapter;

import java.util.List;

import core.model.dto.content.Content;

public record ChapterOutro(
    String title,
    String description,
    List<Content> sneakPeaks,
    List<Content> conclusion
) {
    
}
