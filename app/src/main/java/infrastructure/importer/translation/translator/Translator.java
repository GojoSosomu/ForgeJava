package infrastructure.importer.translation.translator;

import java.util.Map;

public interface Translator<DATA> {
    DATA translate(Map<String, Object> raw, String id);
}
