package infrastructure.importer.translation.mapper;

import java.util.Map;

import core.model.dto.chapter.ChapterDTO;
import infrastructure.importer.translation.maker.chapter.ChapterMaker;

public class ChapterMapper implements Mapper<Map<String, Object>, ChapterDTO> {
    private ChapterMaker maker;

    public ChapterMapper(ChapterMaker maker) {
        this.maker = maker;
    }

    @Override
    public ChapterDTO single(Map<String, Object> raw, String id) {
        if (raw == null || raw.isEmpty()) return null;
        
        ChapterDTO dto = maker.make(raw, id);
        return dto;
    }
}
