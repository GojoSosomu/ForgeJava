package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.chapter.ChapterDTO;
import infrastructure.importer.translation.mapper.ChapterMapper;

public class ChapterTranslator implements Translator<ChapterDTO> {
    private ChapterMapper mapper;

    public ChapterTranslator(
        ChapterMapper mapper
    ) {
        this.mapper = mapper;
    }

    @Override
    public ChapterDTO translate(Map<String, Object> raw, String id) {
        return mapper.single(raw, id);
    }
}
