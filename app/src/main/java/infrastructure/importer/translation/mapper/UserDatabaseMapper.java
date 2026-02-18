package infrastructure.importer.translation.mapper;

import java.util.Map;

import core.model.dto.progress.UserDatabaseDTO;
import infrastructure.importer.translation.maker.progress.UserDatabaseMaker;

public class UserDatabaseMapper implements Mapper<Map<String, Object>, UserDatabaseDTO>{
    private UserDatabaseMaker maker;
    
    public UserDatabaseMapper(
        UserProgressMapper userProgressMapper,
        UserDatabaseMaker maker
    ) {
        this.maker = maker;
    }

    @Override
    public UserDatabaseDTO single(Map<String, Object> raw, String id) {
        if (raw == null || raw.isEmpty()) return null;
        
        return maker.make(raw, id);
    }
}
