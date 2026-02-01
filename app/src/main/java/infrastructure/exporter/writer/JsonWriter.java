package infrastructure.exporter.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.SyncFailedException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter implements Writer {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonWriter() {
    }

    @Override
    public void write(String path, Map<String, Map<String, Object>> data) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to write cannot be null");
        }

        try (FileOutputStream fos = new FileOutputStream(new File(path))) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fos, data);
            fos.flush();

            try {
                fos.getFD().sync();
            } catch (SyncFailedException sfe) {
                System.err.println("Warning: File descriptor sync failed for file: " + path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write JSON file: " + path, e);
        }
    }
}