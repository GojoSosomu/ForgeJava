package presentation.outside.launcher;

import core.engine.Engine;
import core.engine.lifecycle.EngineSetUp;
import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import presentation.service.*;
import presentation.service.assembler.*;
import presentation.utility.IconPathImporter;

public abstract class Launcher {
    protected Engine engine = EngineSetUp.create();
    
    protected ContentViewAssembler contentViewAssembler = new ContentViewAssembler();
    protected LessonViewAssembler lessonViewAssembler = new LessonViewAssembler(contentViewAssembler);
    protected ActivityViewAssembler activityViewAssembler = new ActivityViewAssembler(new ProblemViewAssembler(contentViewAssembler));
    protected ChapterViewAssembler chapterViewAssembler = new ChapterViewAssembler(
        contentViewAssembler, 
        lessonViewAssembler,
        activityViewAssembler
    );

    protected ActivityService activityService = new ActivityService(engine, activityViewAssembler);
    protected UserProgressAssembler userProgressAssembler = new UserProgressAssembler(activityService);
    protected UserService userService = new UserService(userProgressAssembler, engine);

    protected BootService bootService = new BootService(engine);
    protected ChapterService chapterService = new ChapterService(chapterViewAssembler, userProgressAssembler, engine);
    protected LessonService lessonService = new LessonService(engine, lessonViewAssembler);
    protected LogInSignInService logInSignInService = new LogInSignInService(engine);

    protected IconPathImporter iconPathImporter = new IconPathImporter();

    public abstract void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler);
}
