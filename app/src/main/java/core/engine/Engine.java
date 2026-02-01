package core.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.manager.domain.*;
import core.manager.loader.*;
import core.manager.saver.*;
import core.model.dto.DTO;
import core.model.snapshot.chapter.*;

public class Engine {
    private Map<LoadType, LoadTarget> loadTargets = new HashMap<>();
    private Map<SaveType, SaveTarget> saveTargets = new HashMap<>();
    private LessonManager lessonManager;
    private ActivityManager activityManager;
    private ChapterManager chapterManager;
    private UserProgressManager userProgressManager;

    public Engine(
        LessonManager lessonManager, 
        ActivityManager activityManager, 
        ChapterManager chapterManager,
        UserProgressManager userProgressManager
    ) {
        this.lessonManager = lessonManager;
        this.activityManager = activityManager;
        this.chapterManager = chapterManager;
        this.userProgressManager = userProgressManager;

        registerLoadTarget(LoadType.CHAPTER, chapterManager);
        registerLoadTarget(LoadType.LESSON, lessonManager);
        registerLoadTarget(LoadType.ACTIVITY, activityManager);
        registerLoadTarget(LoadType.USER_PROGRESS, userProgressManager);

        registerSaveTarget(SaveType.USER_PROGRESS, userProgressManager);
    }

    public void initialize() {
        lessonManager.printAllLessons();
        activityManager.printAllActivities();
        chapterManager.printAllChapters();
        userProgressManager.printAllUserProgress();
    }

    private void registerLoadTarget(LoadType type, LoadTarget target) {
        loadTargets.put(type, target);
    }

    private void registerSaveTarget(SaveType type, SaveTarget target) {
        saveTargets.put(type, target);
    }

    public void register(LoadType type, String id, DTO dto) {
        LoadTarget target = loadTargets.get(type);
        target.putDTO(id, dto);
    }

    public <T extends DTO> List<T> getAll(SaveType type) {
        List<T> dtos = new ArrayList<>();
        SaveTarget target = saveTargets.get(type);
        Map<String, T> dtoMap = target.getAllDTO();
        dtos.addAll(dtoMap.values());
        return dtos;
    }

    public void newUserAccount(String userName, String password) {
        String salt = userProgressManager.generateSecureSalt();
        String hashedPassword = userProgressManager.hashString(password, salt);
        
        userProgressManager.newUserProgress(userName, hashedPassword, salt);
    }

    public Map<String, ChapterSnapshot> getChapters() {
        List<String> ids = chapterManager.findAll();

        return chapterManager.from(ids);
    }

    public boolean authenticateUser(String userName, String password) {
        String salt = userProgressManager.getSaltByUserName(userName);
        String hashedPassword = userProgressManager.hashString(password, salt);

        return userProgressManager.authenticate(userName, hashedPassword);
    }

    public boolean userAlreadyExists(String userName) {
        return userProgressManager.userExists(userName);
    }
}
