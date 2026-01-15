package infrastructure.importer.translation.maker.content;

import java.util.Map;

import core.model.dto.content.Content;
import infrastructure.importer.translation.maker.Maker;

public abstract class ContentMaker implements Maker<Map<String, Object>, Content> {
    public abstract Content make(Map<String, Object> raw);

    protected String requiredString(Map<String, Object> raw, String variable) {
        Object value = raw.get(variable);
        
        if (value == null) {
            throw new IllegalArgumentException(
                String.format("Missing required field: '%s' in %s", variable, this.getClass().getSimpleName())
            );
        }
        
        String str = value.toString().trim();
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Field '" + variable + "' cannot be empty");
        }
        
        return str;
    }
}
