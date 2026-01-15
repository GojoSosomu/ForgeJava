package core.model.dto.activity.problem;

public enum ProblemType {
    QUESTIONNAIRE;
    
    public static ProblemType fromString(String type) {
        try {
            return ProblemType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Unknown problem type: " + type
                + "\nCheck ProblemType enum for valid types."
            );
        }
    }
}
