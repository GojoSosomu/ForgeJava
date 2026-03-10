package presentation.outside.swing.template.page;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.view.activity.problem.QuestionPageView;
import core.model.view.content.TextContentView;
import core.model.dto.activity.problem.question.QuestionType;
import presentation.outside.renderer.SwingRenderer;
import presentation.service.ActivityService;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingQuestionnairePage extends JPanel {
    private final QuestionPageView data;
    private final SwingRenderer renderer = new SwingRenderer();
    private final List<OptionButton> choices = new ArrayList<>();
    private JTextField textInput;
    private JLabel feedbackStatus;
    private boolean isEvaluated = false;
    private final ActivityService service;

    public SwingQuestionnairePage(QuestionPageView data, ActivityService service) {
        this.data = data;
        this.service = service;
        this.renderer.setGraphics2D((Graphics2D) this.getGraphics().create());

        setOpaque(false);
        setLayout(new BorderLayout(0, 40));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        add(createQuestionDisplay(), BorderLayout.NORTH);

        JPanel interactionArea = new JPanel(new GridBagLayout());
        interactionArea.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        this.renderer.setGraphics2D((Graphics2D) this.getGraphics().create());
        if (data.type() == QuestionType.MULTIPLE_CHOICE) {
            setupMultipleChoice(interactionArea, gbc);
        } else {
            setupTextInput(interactionArea, gbc);
        }

        add(interactionArea, BorderLayout.CENTER);

        feedbackStatus = new JLabel(" ", SwingConstants.CENTER);
        feedbackStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        feedbackStatus.setPreferredSize(new Dimension(0, 50));
        add(feedbackStatus, BorderLayout.SOUTH);
    }

    private JPanel createQuestionDisplay() {
        return new JPanel() {
            { setOpaque(false); setPreferredSize(new Dimension(0, 120)); }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                renderer.setGraphics2D(g2);

                int y = 30;
                for (var text : data.question()) {
                    y = renderer.drawText(text, 0, y, getWidth(), 10, 0, 0);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void setupMultipleChoice(JPanel container, GridBagConstraints gbc) {
        Object rawOptions = data.extras().get("options");
        if (!(rawOptions instanceof List)) return;
        
        List<TextContentView> options = (List<TextContentView>) rawOptions;
        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < options.size(); i++) {
            final int index = i;
            TextContentView textContentView = options.get(i);
            
            String choiceText = textContentView.text();
            renderer.applyStyle(textContentView.style());
            OptionButton opt = new OptionButton(choiceText);
            opt.addActionListener(e -> service.handleAnswerSubmit(index));
            
            group.add(opt);
            choices.add(opt);
            container.add(opt, gbc);
        }
    }

    private void setupTextInput(JPanel container, GridBagConstraints gbc) {
        JLabel label = new JLabel("ENTER RESPONSE:");
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        label.setForeground(INK_DARK);
        container.add(label, gbc);

        textInput = new JTextField();
        textInput.setPreferredSize(new Dimension(400, 50));
        textInput.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_NORMAL, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        container.add(textInput, gbc);
    }

    public void showCorrectFeedback() {
        isEvaluated = true;
        feedbackStatus.setText("TRUTH VERIFIED ✓");
        feedbackStatus.setForeground(SUCCESS_GREEN);
        lockUI(SUCCESS_GREEN);
    }

    public void showErrorFeedback() {
        isEvaluated = true;
        feedbackStatus.setText("LOGICAL ERROR: THE TRUTH WAS DIFFERENT");
        feedbackStatus.setForeground(SCORCH_RED);
        lockUI(SCORCH_RED);
        
        if (data.type() == QuestionType.MULTIPLE_CHOICE) {
            Object corrected = data.extras().get("correctedIndex");
            if (corrected instanceof Integer index && index < choices.size()) {
                choices.get(index).setHighlightColor(SUCCESS_GREEN);
            }
        }
    }

    private void lockUI(Color color) {
        if (data.type() == QuestionType.MULTIPLE_CHOICE) {
            for (OptionButton b : choices) {
                b.setEnabled(false);
                if (b.isSelected()) b.setHighlightColor(color);
            }
        } else if (textInput != null) {
            textInput.setEditable(false);
            textInput.setBorder(BorderFactory.createLineBorder(color, 3));
        }
        repaint();
    }

    private class OptionButton extends JRadioButton {
        private Color highlight = BORDER_NORMAL;

        public OptionButton(String text) {
            super(text);
            setOpaque(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setHighlightColor(Color c) { this.highlight = c; repaint(); }

        @Override
        public Dimension getPreferredSize() { return new Dimension(450, 55); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(isSelected() ? withAlpha(ORANGE_BASED, 40) : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.setColor(isEvaluated ? highlight : (isSelected() ? ORANGE_BASED : BORDER_NORMAL));
            g2.fillRoundRect(12, 12, 6, getHeight() - 24, 4, 4);

            g2.setColor(INK_DARK);
            g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), 45, textY);
            
            g2.dispose();
        }
    }
}