package core.model.view.activity.problem;

import core.model.dto.activity.problem.ProblemType;
import core.model.view.View;

public interface ProblemView extends View {
    ProblemType type();
}
