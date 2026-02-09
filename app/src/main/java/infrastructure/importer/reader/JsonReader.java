package infrastructure.importer.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.*;
import java.util.Collections;
import java.util.Map;

public final class JsonReader implements Reader {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<Map<String, Map<String, Object>>> DATA_TYPE = 
        new TypeReference<>() {};

    @Override
    public Map<String, Map<String, Object>> read(String path) {
        if (path == null || path.isEmpty()) return Collections.emptyMap();

        try (InputStream is = getInputStream(path)) {
            if (is == null) return Collections.emptyMap();

            try (BufferedInputStream bis = new BufferedInputStream(is)) {
                Map<String, Map<String, Object>> data = mapper.readValue(bis, DATA_TYPE);
                return (data == null) ? Collections.emptyMap() : data;
            }
        } catch (MismatchedInputException e) {
            return Collections.emptyMap();
        } catch (Exception e) {
            throw new RuntimeException("JsonReader error: " + path, e);
        }
    }
}