package core.model.view.activity.trial;

import core.model.view.View;

public record TrialView(
    byte amountOfTrial,
    byte maximumOfTrial
) implements View {

    public TrialView incrementTrial() {
        return new TrialView(
            (byte)(this.amountOfTrial + 1), 
            this.maximumOfTrial
        );           
    }
}
