package infrastructure.importer.reader;

import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonReader implements Reader {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonReader() {
    }

    @Override
    public Map<String, Map<String, Object>> read(String path) {
        try {
            if(new File(path).length() == 0) {
                return Map.of();
            }
            return mapper.readValue(new File(path), new TypeReference<Map<String, Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON array: " + path, e);
        }
    }
}

