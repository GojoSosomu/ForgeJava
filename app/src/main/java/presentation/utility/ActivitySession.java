package presentation.utility;

public class ActivitySession {
    private byte attemptLeft = 3;

    public boolean shouldRetry() {
        if (attemptLeft > 1) { // If we have more than 1, we can retry
            attemptLeft--;
            return true;
        }
        return false;
    }

    public byte getAttempt() { return this.attemptLeft; }
}
