package infrastructure.exporter.reconstruction;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import core.model.dto.DTO;

public interface Reconstruction<DATA, RAWDATA extends DTO> {
    DATA reconstruct(RAWDATA rawData, String id);

    default Map<String, DATA> reconstructMap(List<RAWDATA> rawDataList) {
        Map<String, DATA> dataList = new HashMap<>();

        for (int i = 0; i < rawDataList.size(); i++) {
            dataList.put(rawDataList.get(i).id(), reconstruct(rawDataList.get(i), rawDataList.get(i).id()));
        }

        return dataList;
    }
}
