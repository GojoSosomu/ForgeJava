package presentation.utility;

public class ActivitySession {
    private byte attemptLeft = 3;

    public void decrementAttempt() {
        if(attemptLeft >= 0)
            --this.attemptLeft;
    }
}
