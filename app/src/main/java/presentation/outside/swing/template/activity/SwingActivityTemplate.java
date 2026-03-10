package presentation.outside.swing.template.activity;

import core.model.view.progress.info.ScoreView;

public interface SwingActivityTemplate {
    void start(); // For focus and initialization
    ScoreView scoreFinilize(int score, int total);
}