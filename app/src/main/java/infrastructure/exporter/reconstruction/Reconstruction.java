package infrastructure.exporter.reconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import core.model.dto.DTO;

public interface Reconstruction<DATA, RAWDATA extends DTO> {
    DATA reconstruct(RAWDATA rawData, String id);

    default Map<String, DATA> reconstructMap(List<RAWDATA> rawDataList) {
        Map<String, DATA> dataMap = new HashMap<>();
        for (int i = 0; i < rawDataList.size(); i++) {
            String id = rawDataList.get(i).id();
            DATA data = reconstruct(rawDataList.get(i), id);
            dataMap.put(id, data);
        }

        return dataMap;
    }

    default List<DATA> reconstructList(List<RAWDATA> rawDataList) {
        List<DATA> dataList = new ArrayList<>();
        for (int i = 0; i < rawDataList.size(); i++) {
            DATA data = reconstruct(rawDataList.get(i), rawDataList.get(i).id());
            dataList.add(data);
        }

        return dataList;
    }
}
