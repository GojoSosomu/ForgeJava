package core.model.dto.progress.attainment;

import java.util.Set;

public record ActivityProgress(
    Set<String> completedActivityIds
) {

}
