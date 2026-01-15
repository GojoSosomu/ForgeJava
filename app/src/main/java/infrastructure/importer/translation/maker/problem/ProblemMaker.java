package infrastructure.importer.translation.maker.problem;

import java.util.Map;

import core.model.dto.activity.problem.Problem;
import infrastructure.importer.translation.filter.ContentFilter;
import infrastructure.importer.translation.maker.Maker;
import infrastructure.importer.translation.mapper.ContentMapper;

public abstract class ProblemMaker implements Maker<Map<String, Object>, Problem> {
    protected ContentMapper contentMapper;
    protected ContentFilter contentFilter;
    public ProblemMaker(ContentMapper contentMapper, ContentFilter contentFilter) {
        this.contentMapper = contentMapper;
        this.contentFilter = contentFilter;
    }
    
    public abstract Problem make(Map<String, Object> raw);
}
