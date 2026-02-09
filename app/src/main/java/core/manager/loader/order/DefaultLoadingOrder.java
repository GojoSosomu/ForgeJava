package core.manager.loader.order;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import core.manager.loader.LoadType;
import core.manager.loader.LoadingRequest;

public class DefaultLoadingOrder implements LoadingOrder {

    @Override
    public List<LoadingRequest> orders() {
        String lessonPath = getInternalResource("/data/json/lesson.json");
        String activityPath = getInternalResource("/data/json/activity.json");
        String chapterPath = getInternalResource("/data/json/chapter.json");

        String userProgressPath = resolveUserProgressPath();

        return List.of(
            new LoadingRequest("Lesson", lessonPath, LoadType.LESSON),
            new LoadingRequest("Activity", activityPath, LoadType.ACTIVITY),
            new LoadingRequest("Chapter", chapterPath, LoadType.CHAPTER),
            new LoadingRequest("User Progress", userProgressPath, LoadType.USER_PROGRESS)
        );
    }

    private String getInternalResource(String path) {
        var res = getClass().getResource(path);
        if (res == null) throw new RuntimeException("Resource not found: " + path);
        return res.toExternalForm();
    }

    private String resolveUserProgressPath() {
        // In jpackage, this points to the folder with your .exe
        Path rootPath = Paths.get(System.getProperty("user.dir"));
        Path userDir = rootPath.resolve("user_datas");
        
        try {
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            return userDir.resolve("user_progress.json").toAbsolutePath().toString();
        } catch (Exception e) {
            return "user_datas/user_progress.json";
        }
    }
}
