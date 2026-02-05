package infrastructure.importer.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Collections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public final class JsonReader implements Reader {

    private final ObjectMapper mapper;
    private static final TypeReference<Map<String, Map<String, Object>>> DATA_TYPE = 
        new TypeReference<Map<String, Map<String, Object>>>() {};

    public JsonReader() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public Map<String, Map<String, Object>> read(String path) {
        File file = new File(path);

        if (!file.exists() || file.length() == 0) 
            return Collections.emptyMap();
        

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
                        
            Map<String, Map<String, Object>> data = mapper.readValue(bis, DATA_TYPE);
            
            if (data == null || data.isEmpty())
                return Collections.emptyMap();
            
            
            return data;
            
        } catch (MismatchedInputException e) {
            System.out.println("DEBUG: JSON Mismatch/Empty content.");
            return Collections.emptyMap();
        } catch (Exception e) {
            throw new RuntimeException("JsonReader: Error reading " + path + " - " + e.getMessage(), e);
        }
    }
}