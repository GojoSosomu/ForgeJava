package infrastructure.importer.translation.maker.progress;

import java.util.Map;

import core.model.dto.progress.attainment.Score;
import infrastructure.importer.translation.maker.Maker;

public class ScoreMaker implements Maker<Map<String, Object>, Score>{

    @Override
    public Score make(Map<String, Object> raw) {
        return new Score(
            ((Number)raw.get("score")).intValue(), 
            ((Number)raw.get("total")).intValue()
        );
    }
}
