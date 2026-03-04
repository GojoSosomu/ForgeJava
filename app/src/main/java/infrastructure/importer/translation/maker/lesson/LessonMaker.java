package infrastructure.importer.translation.maker.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.dto.lesson.LessonDTO;
import core.model.dto.lesson.LessonPage;
import infrastructure.importer.translation.maker.Maker;
import infrastructure.importer.translation.mapper.ContentMapper;

public class LessonMaker implements Maker<Map<String, Object>, LessonDTO> {
    private ContentMapper contentMapper;
    
    public LessonMaker(
        ContentMapper contentMapper
    ) {
        this.contentMapper = contentMapper;
    }

    @Override
    public LessonDTO make(Map<String, Object> raw, String id) {
        return new LessonDTO(
            id, 
            makeLessonParts((List<Map<String, Object>>) raw.get("pages"))
        );
    }

    private List<LessonPage> makeLessonParts(List<Map<String, Object>> pages) {
        List<LessonPage> result = new ArrayList<>();

        for(Map<String, Object> page : pages) {
            result.add(new LessonPage(
                contentMapper.list((List<Map<String, Object>>)page.get("contents"))
            ));
        }

        return result;
    }
}
