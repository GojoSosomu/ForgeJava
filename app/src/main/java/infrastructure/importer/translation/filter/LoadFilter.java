package infrastructure.importer.translation.filter;

import java.util.List;
import java.util.Map;

public interface LoadFilter<TYPE, DATA, RESULT> {
    List<RESULT> listByType(List<DATA> data, TYPE type);
    Map<String, RESULT> mapByType(Map<String, DATA> data, TYPE type);
}
