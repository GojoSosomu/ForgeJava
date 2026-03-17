package infrastructure.event.receiver;

import core.model.view.activity.trial.TrialView;
import infrastructure.event.pulse.Pulse;
import presentation.outside.channel.OutsideChannel;

public class TrialReceiver implements Pulse<TrialView> {
    private final OutsideChannel<TrialView> channel;

    public TrialReceiver(OutsideChannel<TrialView> channel) {
        this.channel = channel;
    }

    @Override
    public void onPulse(TrialView view) {
        channel.render(view);
    }
}
