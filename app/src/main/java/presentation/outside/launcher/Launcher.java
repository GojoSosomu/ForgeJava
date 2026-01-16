package presentation.outside.launcher;

import core.engine.Engine;
import core.engine.lifecycle.EngineSetUp;
import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import presentation.service.BootService;
import presentation.service.ChapterService;
import presentation.service.assembler.ActivityViewAssembler;
import presentation.service.assembler.ChapterViewAssembler;
import presentation.service.assembler.ContentViewAssembler;
import presentation.service.assembler.LessonViewAssembler;
import presentation.service.assembler.ProblemViewAssembler;
import presentation.service.assembler.ViewAssembler;

public abstract class Launcher {
    protected Engine engine = EngineSetUp.create();

    
    protected ContentViewAssembler contentViewAssembler = new ContentViewAssembler();
    protected ChapterViewAssembler chapterViewAssembler = new ChapterViewAssembler(
        contentViewAssembler, 
        new LessonViewAssembler(contentViewAssembler), 
        new ActivityViewAssembler(new ProblemViewAssembler(contentViewAssembler)));

    protected BootService bootService = new BootService(engine);
    protected ChapterService chapterService = new ChapterService(chapterViewAssembler, engine);


    public abstract void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler);
}
