package core.manager.saver;

import java.util.Map;

import core.model.dto.DTO;

public interface SaveTarget {
    <T extends DTO> Map<String, T> getAllDTO();
}
