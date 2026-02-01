package core.manager.loader.order;

import java.nio.file.Paths;
import java.util.List;

import core.manager.loader.LoadType;
import core.manager.loader.LoadingRequest;

public class DefaultLoadingOrder implements LoadingOrder {

    @Override
    public List<LoadingRequest> orders() {
        String lessonPath;
        String activityPath;
        String chapterPath;
        String userProgressPath;
        try {
            lessonPath = Paths.get(
                DefaultLoadingOrder
                .class
                .getResource("/data/json/lesson.json").toURI())
                .toString()
            ;
            activityPath = Paths.get(
                DefaultLoadingOrder
                .class
                .getResource("/data/json/activity.json").toURI())
                .toString()
            ;
            chapterPath = Paths.get(
                DefaultLoadingOrder
                .class
                .getResource("/data/json/chapter.json").toURI())
                .toString()
            ;
            userProgressPath = Paths.get(
                DefaultLoadingOrder
                .class
                .getResource("/data/json/user_progress.json").toURI())
                .toString()
            ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve lesson.json resource path", e);
        }

        return List.of(
            new LoadingRequest(
                "Lesson",
                lessonPath,
                LoadType.LESSON
            ),
            new LoadingRequest(
                "Activity",
                activityPath,
                LoadType.ACTIVITY
            ),
            new LoadingRequest(
                "Chapter",
                chapterPath,
                LoadType.CHAPTER
            ),
            new LoadingRequest(
                "User Progress",
                userProgressPath,
                LoadType.USER_PROGRESS
            )
        );
    }

}
