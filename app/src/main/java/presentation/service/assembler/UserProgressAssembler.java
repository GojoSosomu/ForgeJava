package presentation.service.assembler;

import java.util.List;
import java.util.Map;

import core.model.snapshot.progress.UserProgressSnapshot;
import core.model.view.progress.UserProgressView;
import core.model.view.progress.info.ProgressInfo;
import core.model.view.progress.info.UserInfo;

public class UserProgressAssembler implements ViewAssembler<UserProgressSnapshot, UserProgressView>{

    @Override
    public UserProgressView from(UserProgressSnapshot snapshot) {
        return new UserProgressView(
            makeUserInfo(snapshot.value()),
            makeProgressInfo(snapshot.value()),
            (int) snapshot.value().get("version")
        );
    }

    private UserInfo makeUserInfo(Map<String, Object> data) {
        return new UserInfo(
            (String) data.get("userName"),
            (String) data.get("password"),
            (String) data.get("salt")
        );
    }

    private ProgressInfo makeProgressInfo(Map<String, Object> data) {
        return new ProgressInfo(
            (List<String>) ((Map<String, Object>) data.get("lessonProgress")).get("completedLessons"),
            (List<String>) ((Map<String, Object>) data.get("chapterProgress")).get("completedChapters"),
            (List<String>) ((Map<String, Object>) data.get("activityProgress")).get("completedActivities"),
            (byte) ((Map<String, Object>)data.get("chapterProgress")).get("currentChapter")
        );
    }
}
