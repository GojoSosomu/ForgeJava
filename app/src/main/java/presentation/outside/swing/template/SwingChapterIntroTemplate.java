package presentation.outside.swing.template;

import java.util.List;

import javax.swing.JPanel;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingChapterIntroTemplate extends JPanel {
    private final List<JPanel> pages;

    public SwingChapterIntroTemplate (
        List<JPanel> pages
    ) {
        this.pages = pages;
    }
}

