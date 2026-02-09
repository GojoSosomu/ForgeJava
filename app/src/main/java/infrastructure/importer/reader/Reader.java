package infrastructure.importer.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

public interface Reader {
    Map<String, Map<String, Object>> read(String path);

    default InputStream getInputStream(String path) throws IOException {
        if (path.startsWith("jar:") || path.startsWith("file:/")) {
            return URI.create(path).toURL().openStream();
        }

        File file = new File(path);
        if (!file.exists() || file.length() == 0) return null;
        return new FileInputStream(file);
    }
}
