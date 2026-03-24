package presentation.outside.interfaces;

import core.model.view.progress.info.ScoreView;

public interface IActivityTemplate {
    void start(); // For focus and initialization
    ScoreView scoreFinilize(int score, int total);
}