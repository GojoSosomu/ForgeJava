package infrastructure.event.receiver;

import core.model.view.progress.info.ScoreView;
import infrastructure.event.pulse.Pulse;
import presentation.outside.channel.OutsideChannel;

public class ScoreReceiver implements Pulse<ScoreView> {
    private final OutsideChannel<ScoreView> channel;

    public ScoreReceiver(OutsideChannel<ScoreView> channel) {
        this.channel = channel;
    }

    @Override
    public void onPulse(ScoreView view) {
        channel.render(view);
    }
}