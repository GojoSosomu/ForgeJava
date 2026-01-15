package core.manager.loader;

import core.model.dto.DTO;

public interface LoadTarget {
    void putDTO(String id, DTO dto);
}
