package core.model.dto.progress.attainment;

import java.util.Set;

public record ChapterProgress(
    Set<String> completedChapters
) {

}
