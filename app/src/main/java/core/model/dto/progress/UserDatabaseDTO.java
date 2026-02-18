package core.model.dto.progress;

import java.util.List;

import core.model.dto.DTO;

public record UserDatabaseDTO(
    List<UserProgressDTO> users,
    String id
) implements DTO{
    
}