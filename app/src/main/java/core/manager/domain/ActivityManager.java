package core.manager.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.domain.assembler.ProblemSnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.activity.ActivityDTO;
import core.model.dto.activity.problem.Questionnaire;
import core.model.dto.activity.problem.question.Question;
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
        String correctKey,
        String correctAnswer,
        boolean isCorrect
    ) {}

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

    // Inside ActivityManager.java
    public EvaulationSnapshot checkAnswer(String activityId, int questionIndex, Object userAnswer) {
        ActivityDTO dto = activityRepository.get(activityId);
        
        if (dto.problem() instanceof Questionnaire questionnaire) {
            Question question = questionnaire.questions().get(questionIndex);
            
            // 1. Get the truth from the DTO (Basement)
            String correctKey = (String) question.values().get("correctAnswerKey");
            boolean isCorrect = userAnswer.equals(correctKey);

            // 2. Create the Evaluation Snapshot (The Report)
            // We put the results in the Map as you designed
            Map<String, Object> reportValues = new HashMap<>();
            reportValues.put("isCorrect", isCorrect);
            reportValues.put("correctKey", correctKey); // Now the Service will know

            return new EvaulationSnapshot(reportValues);
        }
        return null; 
    }
}
