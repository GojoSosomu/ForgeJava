package core.model.view.activity.result;

import java.util.List;

import core.model.view.View;
import core.model.view.activity.evaluation.EvaluationView;

public record QuestionnaireResultView(
    List<EvaluationView> result
) implements View {
    
}
