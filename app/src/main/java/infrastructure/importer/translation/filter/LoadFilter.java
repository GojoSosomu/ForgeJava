package infrastructure.importer.translation.filter;

import java.util.List;

public interface LoadFilter<TYPE, DATA, RESULT> {
    List<RESULT> listByType(List<DATA> data, TYPE type);
}
