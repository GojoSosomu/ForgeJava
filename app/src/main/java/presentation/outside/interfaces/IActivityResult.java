package presentation.outside.interfaces;

import core.model.view.View;

import core.model.view.progress.info.ScoreView;
import presentation.outside.channel.OutsideChannel;

public interface IActivityResult extends OutsideChannel<ScoreView> {
    void showResult();
    void setView(View view);
}
