package core.manager.domain;

import core.manager.loader.LoadTarget;
import core.model.dto.DTO;
import core.model.dto.chapter.ChapterDTO;
import core.repository.ChapterRepository;

public class ChapterManager implements LoadTarget {
    private ChapterRepository chapterRepository;

    public ChapterManager(
        ChapterRepository chapterRepository
    ) {
        this.chapterRepository = chapterRepository;
    }

    public void printAllChapters() {
        chapterRepository.getAll().forEach((id, chapter) -> {
            System.out.println("Chapter ID: " + id);
            System.out.println("Chapter Data: " + chapter +"\n");
        });
    }

    public void putDTO(String id, DTO dto) {
        chapterRepository.register(id, (ChapterDTO) dto);
    }
}
