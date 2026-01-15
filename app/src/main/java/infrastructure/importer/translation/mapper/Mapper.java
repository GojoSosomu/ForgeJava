package infrastructure.importer.translation.mapper;

import java.util.List;

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
}
