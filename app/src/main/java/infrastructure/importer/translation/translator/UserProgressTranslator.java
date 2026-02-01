package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.progress.UserProgressDTO;
import infrastructure.importer.translation.mapper.UserProgressMapper;

public class UserProgressTranslator implements Translator<UserProgressDTO> {
    private UserProgressMapper userProgressMapper;

    public UserProgressTranslator(
        UserProgressMapper userProgressMapper
    ) {
        this.userProgressMapper = userProgressMapper;
    }

    @Override
    public UserProgressDTO translate(Map<String, Object> raw, String id) {
        return userProgressMapper.single(raw, id);
    }
}
