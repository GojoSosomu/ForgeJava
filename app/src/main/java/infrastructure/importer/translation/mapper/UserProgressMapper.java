package infrastructure.importer.translation.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.dto.progress.UserProgressDTO;
import infrastructure.importer.translation.maker.progress.UserProgressMaker;

public class UserProgressMapper implements Mapper<Map<String, Object>, UserProgressDTO> {
    private UserProgressMaker maker;

    public UserProgressMapper(
        UserProgressMaker maker
    ) {
        this.maker = maker;
    }

    @Override
    public UserProgressDTO single(Map<String, Object> raw, String id) {
        if (raw == null || raw.isEmpty()) return null;

        return maker.make(raw, id);
    }

    @Override
    public List<UserProgressDTO> list(List<Map<String, Object>> rawList) {
        if (rawList == null) return List.of();

        List<UserProgressDTO> users = new ArrayList<>();

        for(Map<String, Object> user : rawList) {
            users.add(this.single(user, (String) user.get("id")));
        }

        return users;
    }
}
