package infrastructure.importer.translation.mapper;

import java.util.Map;

import core.model.dto.lesson.LessonDTO;
import infrastructure.importer.translation.maker.lesson.LessonMaker;

public class LessonMapper implements Mapper<Map<String, Object>, LessonDTO> {
    private LessonMaker maker;

    public LessonMapper(
        LessonMaker maker
    ) {
        this.maker = maker;
    }

    @Override
    public LessonDTO single(Map<String, Object> raw, String id) {
        if (raw == null || raw.isEmpty()) return null;
        
        return maker.make(raw, id);
    }
}
