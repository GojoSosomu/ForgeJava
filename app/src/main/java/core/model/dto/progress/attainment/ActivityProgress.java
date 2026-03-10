package core.model.dto.progress.attainment;

import java.util.Map;

public record ActivityProgress(
    Map<String, Score> completedActivities
) {}