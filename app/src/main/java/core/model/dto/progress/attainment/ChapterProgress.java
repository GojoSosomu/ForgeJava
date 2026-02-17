package core.model.dto.progress.attainment;

import java.util.List;

public record ChapterProgress(
    List<String> completedChapters,
    byte currentChapter
) {

}
