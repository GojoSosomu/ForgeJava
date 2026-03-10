package core.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import core.manager.domain.*;
import core.manager.loader.*;
import core.manager.saver.*;
import core.model.dto.DTO;
import core.model.dto.progress.attainment.ActivityProgress;
import core.model.dto.progress.attainment.ChapterProgress;
import core.model.dto.progress.attainment.LessonProgress;
import core.model.dto.progress.attainment.Score;
import core.model.snapshot.activity.ActivitySnapshot;
import core.model.snapshot.activity.evaulation.EvaulationSnapshot;
import core.model.snapshot.chapter.*;
import core.model.snapshot.lesson.LessonSnapshot;
import core.model.snapshot.progress.UserProgressSnapshot;

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
        registerLoadTarget(LoadType.USER_DATABASE, userProgressManager);

        registerSaveTarget(SaveType.USER_DATABASE, userProgressManager);
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

        Collections.sort(ids);

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

    public UserProgressSnapshot getCurrentUser() {
        if(userProgressManager.getCurrentUser() == null) return null;
        String id = userProgressManager.getCurrentUser().id();
        
        return userProgressManager.from(id);
    }

    public String getCurrentUserName() {
        return userProgressManager.getCurrentUser().id();
    }
    
    public void setCurrentUser(String userName) {
        userProgressManager.setCurrentUser(userName);
    }

    public short getCurrentUserSequenceIndex() {
        UserProgressSnapshot user = getCurrentUser();
        return (short) ((Map<String, Object>)user.value().get("chapterProgress")).get("currentSequenceIndex");
    }

    public Map<String, LessonSnapshot> getLessons() {
        List<String> ids = lessonManager.findAll();

        Collections.sort(ids);

        return lessonManager.from(ids);
    }

    private void incrementSequence() {
        userProgressManager.updateProgress(
            getCurrentUserName(), 
            new ChapterProgress(
                userProgressManager.getCurrentUser().chapterProgress().completedChapters(),
                userProgressManager.getCurrentUser().chapterProgress().currentChapter(),
                (short) (userProgressManager.getCurrentUser().chapterProgress().currentSequenceIndex() + 1)
            )
        );
    }

    public void updatedLessonProgress(String id) {
        // Create a NEW mutable list from the old immutable one
        incrementSequence();
        List<String> lessons = new ArrayList<>(userProgressManager.getCurrentUser().lessonProgress().completedLessons());
        lessons.add(id);

        userProgressManager.updateProgress(
            getCurrentUserName(), 
            new LessonProgress(lessons)
        );
    }

    public void updatedActivityProgress(String id, int score, int total) {
        // Create a NEW mutable list from the old immutable one
        Map<String, Score> activitys = new HashMap<>(userProgressManager.getCurrentUser().activityProgress().completedActivities());
        if(!userProgressManager.getCurrentUser().activityProgress().completedActivities().containsKey(id)) {
            incrementSequence();
        }

        activitys.put(id, new Score(
                score,
                total
        ));
        userProgressManager.updateProgress(
            getCurrentUserName(), 
            new ActivityProgress(activitys)
        );
    }

    public Map<String, ActivitySnapshot> getActivitys() {
        List<String> ids = activityManager.findAll();

        Collections.sort(ids);

        return activityManager.from(ids);
    }

    public EvaulationSnapshot evaluateActivityAnswer(String id, int questionIndex, Object answer) {
        return activityManager.checkAnswer(id, questionIndex, answer);
    }
}
