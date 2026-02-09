package infrastructure.exporter.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class JsonWriter implements Writer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void write(String pathString, Map<String, Map<String, Object>> rawListInfo) {
        Path targetPath = Paths.get(pathString);
        Path tempPath = targetPath.resolveSibling(targetPath.getFileName() + ".tmp");

        try {
            if (targetPath.getParent() != null) {
                Files.createDirectories(targetPath.getParent());
            }

            try (OutputStream os = Files.newOutputStream(tempPath);
                 BufferedOutputStream bos = new BufferedOutputStream(os)) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(bos, rawListInfo);
            }

            Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {}
        }
    }
}
