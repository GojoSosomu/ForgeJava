package core.model.view.activity.evaulation;

import core.model.view.View;

public record EvaulationView(
    String message,
    String rightAnswer,
    boolean isUserCorrect
) implements View {

}
