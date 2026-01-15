package infrastructure.importer.translation.mapper;

import java.util.Map;

import core.model.dto.activity.problem.Problem;
import core.model.dto.activity.problem.ProblemType;
import infrastructure.importer.translation.maker.problem.ProblemMaker;

public final class ProblemMapper implements Mapper<Map<String, Object>, Problem> {
    private Map<ProblemType, ProblemMaker> makers;

    public ProblemMapper(Map<ProblemType, ProblemMaker> makers) {
        this.makers = makers;
    }

    @Override
    public Problem single(Map<String, Object> wrapper) {
        if (wrapper == null || wrapper.isEmpty()) return null;
        String typeKey = (String) wrapper.get("type");
        ProblemType type = ProblemType.fromString(typeKey);
        return makers.get(type).make(wrapper);
    }
}
