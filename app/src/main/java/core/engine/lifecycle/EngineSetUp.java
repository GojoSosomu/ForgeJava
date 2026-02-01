package core.engine.lifecycle;

import core.engine.Engine;
import core.manager.domain.*;
import core.manager.domain.assembler.ContentSnapshotAssembler;
import core.manager.domain.assembler.LessonPageSnapshotAssembler;
import core.repository.*;

public class EngineSetUp {

    public static Engine create() {
        ContentSnapshotAssembler contentSnapshotAssembler = new ContentSnapshotAssembler();

        LessonManager lessonManager = new LessonManager(
            new LessonRepository(),
            new LessonPageSnapshotAssembler(contentSnapshotAssembler)
        );

        ActivityManager activityManager = new ActivityManager(
            new ActivityRepository()
        );

        ChapterManager chapterManager = new ChapterManager(
            new ChapterRepository(),
            lessonManager,
            activityManager,
            contentSnapshotAssembler
        );

        UserProgressManager userProgressManager = new UserProgressManager(
            new UserProgressRepository()
        );

        Engine engine = new Engine(lessonManager, activityManager, chapterManager, userProgressManager);
        return engine;
    } 
}
