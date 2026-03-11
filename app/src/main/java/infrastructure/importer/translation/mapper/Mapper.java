package infrastructure.importer.translation.mapper;

import java.util.List;
import java.util.Map;

public interface Mapper<DATA, RESULT> {
    default RESULT single(DATA raw, String id) {
        return null;
    }
    default RESULT single(DATA raw) {
        return null;
    }
    default List<RESULT> list(List<DATA> rawList) {
        return null;
    }
    default Map<String, RESULT> map(Map<String, DATA> rawMap) {
        return null;
    }
}
