package infrastructure.importer.translation.translator;

import java.util.Map;

import core.model.dto.activity.ActivityDTO;
import core.model.dto.activity.problem.Problem;
import infrastructure.importer.translation.mapper.ProblemMapper;

public class ActivityTranslator implements Translator<ActivityDTO> {
    private ProblemMapper mapper;

    public ActivityTranslator(
        ProblemMapper mapper
    ) {
        this.mapper = mapper;
    }

    @Override
    public ActivityDTO translate(Map<String, Object> raw, String id) {
        Map<String, Object> rawProblem = 
            (Map<String, Object>) raw.get("problem");

        Problem problem = mapper.single(rawProblem);

        return new ActivityDTO(id, problem);
    }
}
