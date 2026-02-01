package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.DTO;

public interface Translator<DATA extends DTO> {
    DATA translate(Map<String, Object> raw, String id);
}
