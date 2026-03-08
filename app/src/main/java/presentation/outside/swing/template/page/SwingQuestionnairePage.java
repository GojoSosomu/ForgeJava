package presentation.outside.swing.template.page;

import javax.swing.*;
import java.awt.*;
import core.model.view.activity.problem.QuestionPageView;
import presentation.outside.renderer.SwingRenderer;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingQuestionnairePage extends JPanel {
    private final QuestionPageView data;
    private final SwingRenderer renderer = new SwingRenderer();
    private ButtonGroup optionsGroup;

    public SwingQuestionnairePage(QuestionPageView data) {
        this.data = data;
        setOpaque(false);
        setLayout(new BorderLayout());
        // Setup UI, Question Text, and Radio Buttons here...
    }

    public void showErrorFeedback() {
        // Flash Red or show message
    }
}