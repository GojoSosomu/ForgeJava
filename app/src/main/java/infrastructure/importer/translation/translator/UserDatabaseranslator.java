package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.progress.UserDatabaseDTO;
import infrastructure.importer.translation.mapper.UserDatabaseMapper;

public class UserDatabaseranslator implements Translator<UserDatabaseDTO> {
    private UserDatabaseMapper userDatabaseMapper;

    public UserDatabaseranslator(
        UserDatabaseMapper userDatabaseMapper
    ) {
        this.userDatabaseMapper = userDatabaseMapper;
    }

    @Override
    public UserDatabaseDTO translate(Map<String, Object> raw, String id) {
        return userDatabaseMapper.single(raw, id);
    }
}
