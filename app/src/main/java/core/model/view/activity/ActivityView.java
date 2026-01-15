package core.model.view.activity;

import core.model.view.View;
import core.model.view.activity.problem.ProblemView;

public record ActivityView(
    ProblemView problemView
) implements View {

}
