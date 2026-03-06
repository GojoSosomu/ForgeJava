package infrastructure.importer.translation.maker.chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.dto.chapter.ChapterCard;
import core.model.dto.chapter.ChapterDTO;
import core.model.dto.chapter.ChapterIntro;
import core.model.dto.chapter.ChapterOutro;
import core.model.dto.content.Content;
import core.model.dto.content.TextContent;
import core.model.dto.content.enums.ContentType;
import infrastructure.importer.translation.filter.ContentFilter;
import infrastructure.importer.translation.maker.Maker;
import infrastructure.importer.translation.mapper.ContentMapper;

public class ChapterMaker implements Maker<Map<String, Object>, ChapterDTO> {
    private ContentMapper mapper;
    private ContentFilter filter;

    public ChapterMaker(ContentMapper mapper, ContentFilter filter) {
        this.mapper = mapper;
        this.filter = filter;
    }

    @Override
    public ChapterDTO make(Map<String, Object> raw, String id) {
        Map<String, Object> chapterCard = (Map<String, Object>) raw.get("chapterCard");
        Map<String, Object> chapterIntro = (Map<String, Object>) raw.get("chapterIntro");
        List<String> sequences = (List<String>) raw.get("sequences");
        Map<String, Object> chapterOutro = (Map<String, Object>) raw.get("chapterOutro");
        return new ChapterDTO(
                id,
                new ChapterCard(
                    (TextContent) mapper.single((Map<String, Object>) chapterCard.get("title")  ),
                    (TextContent) mapper.single((Map<String, Object>) chapterCard.get("subTitle")),
                    (TextContent) mapper.single((Map<String, Object>) chapterCard.get("message"))
                ),
                makChapterIntro(chapterIntro),
                sequences,
                new ChapterOutro(
                    (TextContent) mapper.single((Map<String, Object>) chapterOutro.get("title")),
                    (TextContent) mapper.single((Map<String, Object>) chapterOutro.get("description")),
                    mapper.list((List<Map<String, Object>>) chapterOutro.get("sneakPeaks")),
                    mapper.list((List<Map<String, Object>>) chapterOutro.get("conclusion"))
                )
        );
    }

    private ChapterIntro makChapterIntro(Map<String, Object> data) {
        List<TextContent> objectivTextContents = new ArrayList<>();

        List<Content> objectiveTextContentsOnly = filter.listByType(
                mapper.list(
                    (List<Map<String, Object>>) 
                        data.get("objectives")
                ),
                ContentType.TEXT
        );

        for(Content content : objectiveTextContentsOnly) {
            objectivTextContents.add((TextContent)content);
        }

        return new ChapterIntro(
            (TextContent) mapper.single((Map<String, Object>) data.get("title")),
            (TextContent) mapper.single((Map<String, Object>) data.get("description")),
            objectivTextContents
        );
    }

    @Override
    public ChapterDTO make(Map<String, Object> raw) {
        return null;
    }
}
