package core.model.view.progress.info;

public record ScoreView(
    int score, 
    int total, 
    String status
) {}