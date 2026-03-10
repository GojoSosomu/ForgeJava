package core.model.view.activity.evaulation;

import core.model.view.View;

public record EvaulationView(
    String rightAnswer,
    boolean isCorrect,
    int correctIndex
) implements View {

}
