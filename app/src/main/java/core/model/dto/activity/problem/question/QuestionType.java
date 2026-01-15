package core.model.dto.activity.problem.question;

public enum QuestionType {
    MULTIPLE_CHOICE,
    TEXT;

    public static QuestionType fromString(String type) {
        try {
            return QuestionType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Unknown problem type: " + type
                + "\nCheck ProblemType enum for valid types."
            );
        }
    }
}
