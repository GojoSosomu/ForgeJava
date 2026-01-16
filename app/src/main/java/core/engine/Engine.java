package core.engine;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import core.manager.domain.*;
import core.manager.loader.*;
import core.model.dto.DTO;
import core.model.snapshot.chapter.*;

public class Engine {
    private Map<LoadType, LoadTarget> loadTargets = new HashMap<>();
    private LessonManager lessonManager;
    private ActivityManager activityManager;
    private ChapterManager chapterManager;

    public Engine(
        LessonManager lessonManager, 
        ActivityManager activityManager, 
        ChapterManager chapterManager
    ) {
        this.lessonManager = lessonManager;
        this.activityManager = activityManager;
        this.chapterManager = chapterManager;

        registerLoadTarget(LoadType.CHAPTER, chapterManager);
        registerLoadTarget(LoadType.LESSON, lessonManager);
        registerLoadTarget(LoadType.ACTIVITY, activityManager);
    }

    public void initialize() {
        lessonManager.printAllLessons();
        activityManager.printAllActivities();
        chapterManager.printAllChapters();
    }

    private void registerLoadTarget(LoadType type, LoadTarget target) {
        loadTargets.put(type, target);
    }

    public void register(LoadType type, String id, DTO dto) {
        LoadTarget target = loadTargets.get(type);
        target.putDTO(id, dto);
    }

    public Map<String, ChapterSnapshot> getChapters() {
        List<String> ids = chapterManager.findAll();

        return chapterManager.from(ids);
    }
}
