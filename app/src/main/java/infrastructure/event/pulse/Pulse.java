package infrastructure.event.pulse;

import core.model.snapshot.Snapshot;

public interface Pulse<T extends Snapshot> {
    void onPulse(T Snapshot);
}
