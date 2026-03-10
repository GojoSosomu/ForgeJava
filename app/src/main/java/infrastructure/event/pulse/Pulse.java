package infrastructure.event.pulse;

public interface Pulse<T> {
    void onPulse(T Snapshot);
}
