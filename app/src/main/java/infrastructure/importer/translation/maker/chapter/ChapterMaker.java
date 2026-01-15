package infrastructure.importer.translation.maker.chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.dto.chapter.ChapterCard;
import core.model.dto.chapter.ChapterDTO;
import core.model.dto.chapter.ChapterIntro;
import core.model.dto.chapter.ChapterOutro;
import core.model.dto.content.Content;
import core.model.dto.content.ContentType;
import core.model.dto.content.TextContent;
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
                    (String) chapterCard.get("title"),
                    (String) chapterCard.get("subTitle"),
                    (TextContent) mapper.single((Map<String, Object>) chapterCard.get("message"))
                ),
                makChapterIntro(chapterIntro),
                sequences,
                new ChapterOutro(
                    (String) chapterOutro.get("title"),
                    (String) chapterOutro.get("description"),
                    mapper.list((List<Map<String, Object>>) chapterOutro.get("sneakPeaks")),
                    mapper.list((List<Map<String, Object>>) chapterOutro.get("conclusion"))
                )
        );
    }

    private ChapterIntro makChapterIntro(Map<String, Object> data) {
        List<TextContent> objectivTextContents = new ArrayList<>();
        List<TextContent> descriptiTextContents = new ArrayList<>();
        List<Content> objectiveTextContentsOnly = filter.listByType(
                mapper.list(
                    (List<Map<String, Object>>) 
                        data.get("objectives")
                ),
                ContentType.TEXT
        );
        List<Content> descriptionTextContentsOnly = filter.listByType(
                mapper.list(
                    (List<Map<String, Object>>) 
                        data.get("objectives")
                ),
                ContentType.TEXT
        );

        for(Content content : objectiveTextContentsOnly) {
            objectivTextContents.add((TextContent)content);
        }
        
        for(Content content : descriptionTextContentsOnly) {
            descriptiTextContents.add((TextContent)content);
        }

        return new ChapterIntro(
                    (String) data.get("title"),
                    descriptiTextContents,
                    objectivTextContents
        );
    }

    @Override
    public ChapterDTO make(Map<String, Object> raw) {
        return null;
    }
}
