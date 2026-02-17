package core.model.view.activity;

import core.model.view.View;
import core.model.view.activity.problem.ProblemView;

public record ActivityView(
    String id,
    ProblemView problemView
) implements View {

}
