package infrastructure.importer.reader;

import java.util.Map;

public interface Reader {
    Map<String, Map<String, Object>> read(String path);
}
