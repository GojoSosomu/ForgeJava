package presentation.service.assembler;

import java.util.Map;
import java.util.Set;

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
            (Set<String>) data.get("lessonProgress"),
            (Set<String>) data.get("chapterProgress"),
            (Set<String>) data.get("activityProgress")
        );
    }
}
