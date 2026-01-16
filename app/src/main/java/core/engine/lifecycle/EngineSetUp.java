package core.engine.lifecycle;

import core.engine.Engine;
import core.manager.domain.ActivityManager;
import core.manager.domain.ChapterManager;
import core.manager.domain.LessonManager;
import core.manager.domain.assembler.ContentSnapshotAssembler;
import core.manager.domain.assembler.LessonPageSnapshotAssembler;
import core.repository.ActivityRepository;
import core.repository.ChapterRepository;
import core.repository.LessonRepository;

public class EngineSetUp {

    public static Engine create() {
        
        LessonManager lessonManager = new LessonManager(
            new LessonRepository(),
            new LessonPageSnapshotAssembler(new ContentSnapshotAssembler())
        );

        ActivityManager activityManager = new ActivityManager(
            new ActivityRepository()
        );

        ChapterManager chapterManager = new ChapterManager(
            new ChapterRepository(),
            lessonManager,
            activityManager
        );

        Engine engine = new Engine(lessonManager, activityManager, chapterManager);
        return engine;
    } 
}
