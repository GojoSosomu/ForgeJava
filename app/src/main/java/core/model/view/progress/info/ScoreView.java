package core.model.view.progress.info;

import core.model.view.View;

public record ScoreView(
    int score, 
    int total, 
    String status
) implements View {}