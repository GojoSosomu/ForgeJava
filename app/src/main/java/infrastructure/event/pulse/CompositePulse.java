package infrastructure.event.pulse;

import java.util.ArrayList;
import java.util.List;

import core.model.snapshot.Snapshot;

public class CompositePulse<T extends Snapshot> implements Pulse<T> {
    private List<Pulse<T>> pulses = new ArrayList<>();

    public CompositePulse(List<Pulse<T>> pulses) {
        this.pulses = pulses;
    }
    @Override
    public void onPulse(T Snapshot) {
        for(Pulse<T> pulse : pulses) {
            pulse.onPulse(Snapshot);
        }
    }

}
