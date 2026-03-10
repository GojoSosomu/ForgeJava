package core.manager.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.domain.assembler.ProblemSnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.activity.ActivityDTO;
import core.model.dto.activity.problem.ProblemType;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
import core.model.dto.activity.problem.question.QuestionType;
import core.model.snapshot.activity.ActivitySnapshot;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;
import core.repository.ActivityRepository;

public class ActivityManager implements LoadTarget, EntitySnapshotAssembler<ActivitySnapshot> {
    private ActivityRepository activityRepository;
    private ProblemSnapshotAssembler problemSnapshotAssembler = new ProblemSnapshotAssembler();

    public ActivityManager(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public record Evaulation(
        ProblemType problemType,
        QuestionType questionType,
        int correctIndex,
        String correctAnswer
    ) {
    }

    public void printAllActivities() {
        activityRepository.getAll().forEach((id, activity) -> {
            System.out.println("Activity ID: " + id);
            System.out.println("Activity Data: " + activity +"\n");
        });
    }

    public void putDTO(String id, DTO dto) {
        activityRepository.register(id, (ActivityDTO)dto);
    }

    public boolean isExist(String id) {
        return activityRepository.isExist(id);
    }

    @Override
    public ActivitySnapshot from(String id) {
       ActivityDTO dto = activityRepository.get(id);

       return new ActivitySnapshot(
        dto.id(),
        problemSnapshotAssembler.from(dto.problem())
       );
    }

    public List<String> findAll() {
        Map<String, ActivityDTO> activitys = activityRepository.getAll();

        List<String> ids = new ArrayList<>(activitys.keySet());

        return ids;
    }

    public EvaulationSnapshot checkAnswer(String activityId, int questionIndex, Object userAnswer) {
        ActivityDTO dto = activityRepository.get(activityId);
        if (dto == null) return false;

        if (dto.problem() instanceof Questionnaire questionnaire) {
            Question question = questionnaire.questions().get(questionIndex);
            
            return switch (question.type()) {
                case MULTIPLE_CHOICE -> {
                    int correct = (int) question.values().get("correctAnswerIndex");
                    yield userAnswer.equals(correct);
                }
            };
        }
        return false;
    }
}
