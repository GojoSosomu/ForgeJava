package core.manager.domain;

import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.lesson.LessonDTO;
import core.repository.LessonRepository;

public class LessonManager implements LoadTarget {
    private LessonRepository lessonRepository;

    public LessonManager(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public void printAllLessons() {
        lessonRepository.getAll().forEach((id, lesson) -> {
            System.out.println("Lesson ID: " + id);
            System.out.println("Lesson Data: " + lesson +"\n");
        });
    }

    public void putDTO(String id, DTO dto) {
        lessonRepository.register(id, (LessonDTO)dto);
    }
}