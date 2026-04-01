package presentation.utility;

import java.util.HashMap;
import java.util.Map;

public class ActivitySessionManager {
    // Stores sessions by Activity ID so they persist
    private static final Map<String, ActivitySession> sessions = new HashMap<>();
    
    public static class ActivitySession {
        private byte attemptLeft = 1;

        public boolean shouldRetry() {
            if (this.attemptLeft > 1) { // If we have more than 1, we can retry
                this.attemptLeft--;
                return true;
            }
            this.attemptLeft = 0;
            return false;
        }

        public byte getAttempt() { return this.attemptLeft; }
    }

    public static ActivitySession getSession(String id) {
        // If it doesn't exist, create it. If it does, return the old one.
        return sessions.computeIfAbsent(id, k -> new ActivitySession());
    }

    public static void removeSession(String id) {
        sessions.remove(id);
    }
}
