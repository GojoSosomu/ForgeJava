package core.manager.loader;

import core.model.dto.DTO;

public interface LoadTarget {
    <T extends DTO> void putDTO(String id, T dto);
}
