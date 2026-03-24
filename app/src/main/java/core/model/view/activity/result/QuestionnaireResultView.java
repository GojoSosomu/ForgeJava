package core.model.view.activity.result;

import java.util.List;

import core.model.view.View;
import core.model.view.activity.evaulation.EvaulationView;

public record QuestionnaireResultView(
    List<EvaulationView> result
) implements View {
    
}
