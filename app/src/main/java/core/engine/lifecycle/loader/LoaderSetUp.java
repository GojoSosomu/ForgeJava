
package core.engine.lifecycle.loader;

import java.util.Map;

import core.engine.Engine;
import core.manager.loader.*;
import core.manager.loader.order.*;
import core.model.dto.activity.*;
import core.model.dto.activity.problem.*;
import core.model.dto.chapter.ChapterDTO;
import core.model.dto.content.ContentType;
import core.model.dto.lesson.LessonDTO;
import core.model.dto.progress.UserDatabaseDTO;
import infrastructure.event.receiver.LoadingReceiver;
import infrastructure.importer.DataSizer;
import infrastructure.importer.reader.*;
import infrastructure.importer.translation.filter.*;
import infrastructure.importer.translation.maker.chapter.*;
import infrastructure.importer.translation.maker.content.*;
import infrastructure.importer.translation.maker.lesson.*;
import infrastructure.importer.translation.maker.problem.*;
import infrastructure.importer.translation.maker.progress.*;
import infrastructure.importer.translation.mapper.*;
import infrastructure.importer.translation.translator.*;

public class LoaderSetUp {
    
    public static LoaderExecutor create(
        Engine engine,
        LoadingReceiver loadingReceiver
    ) {
        // Initialize the Filters
        ContentFilter contentFilter = new ContentFilter();

        // Setup Mappers
        Map<ContentType, ContentMaker> contentTypeMap = Map.of(
            ContentType.TEXT, new TextContentMaker(),
            ContentType.IMAGE, new ImageContentMaker(),
            ContentType.VIDEO, new VideoContentMaker()
        );
        ContentMapper contentMapper = new ContentMapper(contentTypeMap);

        Map<ProblemType, ProblemMaker> problemTypeMap = Map.of(
            ProblemType.QUESTIONNAIRE, new QuestionnaireMaker(contentMapper, contentFilter)
        );
        ProblemMapper problemMapper = new ProblemMapper(problemTypeMap);

        ChapterMaker chapterMaker = new ChapterMaker(contentMapper, contentFilter);
        ChapterMapper chapterMapper = new ChapterMapper(chapterMaker);

        LessonMaker lessonMaker = new LessonMaker(contentMapper);
        LessonMapper lessonMapper = new LessonMapper(lessonMaker);

        UserProgressMaker userProgressMaker = new UserProgressMaker();
        UserProgressMapper userProgressMapper = new UserProgressMapper(userProgressMaker);

        UserDatabaseMaker userDatabaseMaker = new UserDatabaseMaker(userProgressMapper);
        UserDatabaseMapper userDatabaseMapper = new UserDatabaseMapper(userProgressMapper, userDatabaseMaker);

        // Initialize JsonImporter
        Reader jsonReader = new JsonReader();

        // Initialize DataSizer
        DataSizer sizer = new DataSizer(jsonReader);
        
        // Initialize JsonLoaders, LoaderTasks, and add to MasterLoader
        Translator<LessonDTO> lessonTranslator = new LessonTranslator(lessonMapper);
        
        Loader<LessonDTO> lessonLoader = new Loader<>(
            lessonTranslator, 
            jsonReader,
            engine
        );

        Translator<ActivityDTO> activityTranslator = new ActivityTranslator(
            problemMapper
        );
        
        Loader<ActivityDTO> activityLoader = new Loader<>(
            activityTranslator, 
            jsonReader,
            engine
        );


        Translator<ChapterDTO> chapterTranslator = new ChapterTranslator(chapterMapper);
        
        Loader<ChapterDTO> chapterLoader = new Loader<>(
            chapterTranslator,
            jsonReader,
            engine
        );

        Translator<UserDatabaseDTO> userDatabaseTranslator = new UserDatabaseranslator(userDatabaseMapper);

        Loader<UserDatabaseDTO> userDatabaseLoader = new Loader<>(
            userDatabaseTranslator,
            jsonReader,
            engine
        );

        // Initialize LoaderExecutor
        Map<LoadType, Loader<?>> loaders = Map.of(
            LoadType.LESSON, lessonLoader,
            LoadType.ACTIVITY, activityLoader,
            LoadType.CHAPTER, chapterLoader,
            LoadType.USER_DATABASE, userDatabaseLoader
        );

        LoaderExecutor loaderExecutor = new LoaderExecutor(
            loaders,
            new DefaultLoadingOrder(),
            loadingReceiver,
            sizer
        );

        return loaderExecutor;
    }
}
