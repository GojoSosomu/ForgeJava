package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.lesson.LessonDTO;
import infrastructure.importer.translation.mapper.LessonMapper;

public class LessonTranslator implements Translator<LessonDTO> {
    private LessonMapper lessonMapper;

    public LessonTranslator(
        LessonMapper lessonMapper
    ) {
        this.lessonMapper = lessonMapper;
    }

    @Override
    public LessonDTO translate(Map<String, Object> raw, String id) {
        return lessonMapper.single(raw, id);
    }
}
