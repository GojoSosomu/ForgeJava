package infrastructure.exporter.writer;

import java.util.Map;

public interface Writer {
    void write(String path, Map<String,Map<String,Object>> rawListInfo);
}
