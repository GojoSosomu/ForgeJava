package infrastructure.importer.translation.maker.progress;

import java.util.List;
import java.util.Map;

import core.model.dto.progress.UserDatabaseDTO;
import core.model.dto.progress.UserProgressDTO;
import infrastructure.importer.translation.maker.Maker;
import infrastructure.importer.translation.mapper.UserProgressMapper;

public class UserDatabaseMaker implements Maker<Map<String, Object>, UserDatabaseDTO>{
    private UserProgressMapper mapper;

    public UserDatabaseMaker(
        UserProgressMapper mapper
    ) {
        this.mapper = mapper;
    }
    @Override
    public UserDatabaseDTO make(Map<String, Object> raw, String id) {
        return new UserDatabaseDTO(
            makeAllUsers(raw),
            getCurrentUserID(raw)
        );
    }

    private String getCurrentUserID(Map<String, Object> raw) {
        return (String) raw.get("id");
    }

    private List<UserProgressDTO> makeAllUsers(Map<String, Object> raw) {
        return mapper.list((List<Map<String, Object>>) raw.get("users"));
    }
}
