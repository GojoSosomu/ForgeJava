package presentation.utility;

import java.util.List;

public class ChapterCarouselUtility {

    private final List<String> chapterIds;
    private int currentIndex = 0;

    public ChapterCarouselUtility(List<String> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public String getCurrentChapterId() {
        return chapterIds.get(currentIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int size() {
        return chapterIds.size();
    }

    public boolean moveLeft() {
        currentIndex = (currentIndex - 1 + size()) % size();
        return true;
    }

    public boolean moveRight() {
        currentIndex = (currentIndex + 1 + size()) % size();
        return true;
    }

    public void selectIndex(int index) {
        if (index < 0 || index >= chapterIds.size()) return;
        currentIndex = index;
    }
}