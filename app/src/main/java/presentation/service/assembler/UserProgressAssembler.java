package presentation.service.assembler;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.model.snapshot.progress.ScoreSnapshot;
import core.model.snapshot.progress.UserProgressSnapshot;
import core.model.view.progress.UserProgressView;
import core.model.view.progress.info.ProgressInfo;
import core.model.view.progress.info.ScoreView;
import core.model.view.progress.info.UserInfo;
import presentation.service.ActivityService;

public class UserProgressAssembler implements ViewAssembler<UserProgressSnapshot, UserProgressView>{
    private final ActivityService service;

    public UserProgressAssembler(
        ActivityService service
    ) {
        this.service = service;
    }

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
            makeScores((Map<String, ScoreSnapshot>)((Map<String, Object>) data.get("activityProgress")).get("completedActivities")),
            ((Number)((Map<String, Object>)data.get("chapterProgress")).get("currentChapter")).byteValue()        
        );
    }

    private Map<String, ScoreView> makeScores(Map<String, ScoreSnapshot> data) {
        Map<String, ScoreView> scoreViews = new TreeMap<>();
        data.forEach((id, snap) -> {
            scoreViews.put(id, makeScore(snap));
        });

        return scoreViews;
    }

    private ScoreView makeScore(ScoreSnapshot score) {
        String status = service.checkStatus(score.score(), score.total());
        return new ScoreView(score.score(), score.total(), status);
    }
}
