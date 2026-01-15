package core.manager.domain;

import java.util.ArrayList;
import java.util.List;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.domain.assembler.ProblemSnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.activity.ActivityDTO;
import core.model.snapshot.activity.ActivitySnapshot;
import core.repository.ActivityRepository;

public class ActivityManager implements LoadTarget, EntitySnapshotAssembler<ActivitySnapshot> {
    private ActivityRepository activityRepository;
    private ProblemSnapshotAssembler problemSnapshotAssembler = new ProblemSnapshotAssembler();

    public ActivityManager(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
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

    @Override
    public List<ActivitySnapshot> from(List<String> ids) {
        List<ActivitySnapshot> result = new ArrayList<>();

        for(String id : ids) {
            result.add(this.from(id));
        }

        return result;
    }

    @Override
    public ActivitySnapshot from(String id) {
       ActivityDTO dto = activityRepository.get(id);

       return new ActivitySnapshot(
        dto.id(),
        problemSnapshotAssembler.from(dto.problem())
       );
    }
}
