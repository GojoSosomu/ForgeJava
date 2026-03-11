package infrastructure.importer.translation.mapper;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import core.model.dto.progress.attainment.Score;
import infrastructure.importer.translation.maker.progress.ScoreMaker;

public class ScoreMapper implements Mapper<Map<String, Object>, Score> {
    private ScoreMaker maker;

    public ScoreMapper(
        ScoreMaker maker
    ) {
        this.maker = maker;
    }

    @Override
    public Score single(Map<String, Object> raw) {
        if (raw == null || raw.isEmpty()) return null;
        
        return maker.make(raw);
    }

    @Override
    public Map<String, Score> map(Map<String, Map<String, Object>> rawMap) {
        Map<String, Score> scores = new TreeMap<>();

        for(Entry<String, Map<String, Object>> score : rawMap.entrySet()) {
            scores.put(score.getKey(), this.single(score.getValue()));
        }

        return scores;
    }
}
