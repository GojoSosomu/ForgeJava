package infrastructure.exporter.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter implements Writer {
    private final ObjectMapper mapper;

    public JsonWriter() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void write(String path, Map<String, Map<String, Object>> rawListInfo) {
        File targetFile = new File(path);
        File tempFile = new File(path + ".tmp");

        if (targetFile.getParentFile() != null) {
            targetFile.getParentFile().mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(bos, rawListInfo);
            bos.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            if (tempFile.exists()) tempFile.delete();
            return; 
        }

        try {
            if (targetFile.exists())
                if (!targetFile.delete())
                    throw new Exception("Could not delete existing target file: " + path);
            

            boolean renamed = tempFile.renameTo(targetFile);

            if (!renamed) 
                throw new Exception("Rename operation failed.");
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
