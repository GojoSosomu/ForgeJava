package core.model.dto.activity;

import core.model.dto.DTO;
import core.model.dto.activity.problem.Problem;

public record ActivityDTO(
    String id,
    Problem problem
) implements DTO {
    
}
