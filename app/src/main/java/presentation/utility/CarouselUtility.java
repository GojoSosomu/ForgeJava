package presentation.utility;

import java.util.List;

public class CarouselUtility<T> {

    private final List<T> items;
    private int currentIndex = 0;
    private boolean wrapAround = true; // Optional: toggle for Intro/Outro

    public CarouselUtility(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Carousel cannot be empty");
        }
        this.items = items;
    }

    public T getCurrentItem() {
        return items.get(currentIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int size() {
        return items.size();
    }

    public boolean moveLeft() {
        if (wrapAround) {
            currentIndex = (currentIndex - 1 + size()) % size();
            return true;
        } else if (currentIndex > 0) {
            currentIndex--;
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if (wrapAround) {
            currentIndex = (currentIndex + 1) % size();
            return true;
        } else if (currentIndex < size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public void selectIndex(int index) {
        if (index >= 0 && index < size()) {
            this.currentIndex = index;
        }
    }
    
    public void setWrapAround(boolean wrap) {
        this.wrapAround = wrap;
    }
}