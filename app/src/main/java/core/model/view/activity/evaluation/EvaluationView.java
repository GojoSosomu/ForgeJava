package core.model.view.activity.evaluation;

import core.model.view.View;

public record EvaluationView(
    String message,
    String rightAnswer,
    boolean isUserCorrect
) implements View {

}
