package presentation.outside.swing.template.page;

import static presentation.outside.library.LibraryOfColor.BORDER_NORMAL;
import static presentation.outside.library.LibraryOfColor.INK_DARK;
import static presentation.outside.library.LibraryOfColor.ORANGE_BASED;
import static presentation.outside.library.LibraryOfColor.ORANGE_HOVER;
import static presentation.outside.library.LibraryOfColor.SCORCH_RED;
import static presentation.outside.library.LibraryOfColor.SUCCESS_GREEN;
import static presentation.outside.library.LibraryOfColor.withAlpha;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.model.dto.activity.problem.question.QuestionType;
import core.model.dto.content.enums.text.TextEmphasize;
import core.model.dto.content.enums.text.TextSize;
import core.model.dto.content.enums.text.TextStyle;
import core.model.view.activity.evaulation.EvaulationView;
import core.model.view.activity.problem.QuestionPageView;
import core.model.view.content.TextContentView;
import presentation.outside.renderer.SwingRenderer;
import presentation.service.ActivityService;

public class SwingQuestionnairePage extends JPanel {
    private final QuestionPageView data;
    private final SwingRenderer renderer = new SwingRenderer();
    private final List<OptionButton> choices = new ArrayList<>();
    private JTextField textInput;
    private JLabel feedbackStatus;
    private JButton nextButton;
    private boolean isEvaluated = false, isCorrect = false;
    private final ActivityService service;
    private final String id;
    private final Runnable onPressed;
    private final String questionNumber;
    private EvaulationView evaluationView;

    public SwingQuestionnairePage(
        String id,
        String questionNumber,
        QuestionPageView data, 
        ActivityService service,
        Runnable onPressed
    ) {
        this.data = data;
        this.service = service;
        this.id = id;
        this.questionNumber = questionNumber;
        this.onPressed = onPressed;

        setOpaque(false);
        setLayout(new BorderLayout(0, 40));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        add(createQuestionDisplay(), BorderLayout.NORTH);

        JPanel interactionWrapper = new JPanel(new BorderLayout());
        interactionWrapper.setOpaque(false);

        // 2. The Button Container (Using BoxLayout for perfect vertical stacking)
        JPanel interactionArea = new JPanel();
        interactionArea.setOpaque(false);

        if (data.type() == QuestionType.MULTIPLE_CHOICE) {
            setupMultipleChoice(interactionArea);
        }
        // 3. Add "Glue" at the bottom to push everything up
        interactionArea.add(Box.createVerticalGlue());

        // 4. Put the stack into the wrapper's North so it never stretches
        interactionWrapper.add(interactionArea, BorderLayout.NORTH);


        add(interactionArea, BorderLayout.CENTER);

        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createQuestionDisplay() {
        return new JPanel() {
            { 
                setOpaque(false); 
                // Reserve exactly 120px for the question text
                setPreferredSize(new Dimension(800, 120));
                setMinimumSize(getPreferredSize());
                setMaximumSize(getPreferredSize());
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                renderer.setGraphics2D(g2);

                int currentY = 30;

                String header = questionNumber;
                renderer.applyStyle(new TextStyle(TextEmphasize.TITLE, TextSize.LARGE));
                currentY = renderer.drawText(header, 0, currentY, (float) getWidth(), 15, 0, 5);

                for (var text : data.question()) {
                    currentY = renderer.drawText(text, 0, currentY, (float) getWidth(), 10, 0, 0);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void setupMultipleChoice(JPanel container) {
        Map<String, Object> extras = (Map<String, Object>) data.extras();

        ButtonGroup group = new ButtonGroup();
        List<String> answerOptions = (List<String>)extras.get("answerOptions");
        List<TextContentView> descriptionOptions = (List<TextContentView>)extras.get("descriptionOptions");
        int rowCount = descriptionOptions.size();

        // 1. Create the Fair Grid (Rows = Count, Cols = 1, Gap = 12px)
        JPanel gridPanel = new JPanel(new GridLayout(rowCount, 1, 0, 12));
        gridPanel.setOpaque(false);

        for (int i = 0; i < rowCount; i++) {
            final int index = i;
            OptionButton opt = new OptionButton(descriptionOptions.get(index));
            opt.addActionListener(e -> {
                EvaulationView result = service.evaluate(id, data.questionNumber(), answerOptions.get(index));
                isCorrect = result.isUserCorrect();
                if (result.isUserCorrect()) showCorrectFeedback(result);
                else showErrorFeedback(result);
                evaluationView = result;
            });

            group.add(opt);
            choices.add(opt);
            gridPanel.add(opt); // Add to the grid
        }

        // 2. Use GridBag to anchor the Fair-Grid to the TOP so it never clips the bottom
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Do not stretch vertically
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        container.add(gridPanel, gbc);
    }

   private JPanel createFooterPanel() {
        JPanel footer = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getWidth(), 130); 
            }
        };
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        feedbackStatus = new JLabel(" ", SwingConstants.CENTER);
        feedbackStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        feedbackStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextButton = new JButton("CONTINUE →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SUCCESS_GREEN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        // Lock the button size so it doesn't flex
        nextButton.setMinimumSize(new Dimension(220, 50));
        nextButton.setPreferredSize(new Dimension(220, 50));
        nextButton.setMaximumSize(new Dimension(220, 50));

        nextButton.setContentAreaFilled(false);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setVisible(false);

        nextButton.addActionListener(e -> onPressed.run());

        footer.add(feedbackStatus);
        footer.add(Box.createRigidArea(new Dimension(0, 15)));
        footer.add(nextButton);

        return footer;
    }

    public void showCorrectFeedback(EvaulationView evaulationView) {
        isEvaluated = true;
        feedbackStatus.setText(evaulationView.message());
        feedbackStatus.setForeground(SUCCESS_GREEN);
        nextButton.setVisible(true); // SHOW THE NEXT STEP
        lockUI(SUCCESS_GREEN);
    }

    public void showErrorFeedback(EvaulationView evaulationView) {
        isEvaluated = true;
        feedbackStatus.setText(evaulationView.message());
        feedbackStatus.setForeground(SCORCH_RED);
        nextButton.setVisible(true); // SHOW THE NEXT STEP
        lockUI(SCORCH_RED);
    }

    private void lockUI(Color resultColor) {
        if (data.type() == QuestionType.MULTIPLE_CHOICE) {
            for (OptionButton b : choices) {
                b.setEnabled(false);
                
                // If this was the user's choice, give it the result color (Success or Error)
                if (b.isSelected()) {
                    b.setHighlightColor(resultColor);
                } else {
                    // Keep others neutral but set them to the evaluated state
                    b.setHighlightColor(BORDER_NORMAL); 
                }
            }
        } else if (textInput != null) {
            textInput.setEditable(false);
            textInput.setBorder(BorderFactory.createLineBorder(resultColor, 3));
        }
        repaint();
    }

    // --- Updated OptionButton to be rigid ---
    private class OptionButton extends JRadioButton {

        private final TextContentView content;

        private Color highlight = BORDER_NORMAL;
        private boolean isHovered = false;

        public OptionButton(TextContentView content) {
            super(content.text());
            this.content = content;

            setOpaque(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (isEnabled()) {
                        isHovered = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public void setHighlightColor(Color c) {
            this.highlight = c;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(480, 30);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth() - 1;
            int h = getHeight() - 1;
            int arc = 18;

            /*
            --------------------------------
            Background
            --------------------------------
            */

            if (isEvaluated && highlight != BORDER_NORMAL) {
                g2.setColor(withAlpha(highlight, 25));
            }
            else if (isSelected()) {
                g2.setColor(withAlpha(ORANGE_BASED, 20));
            }
            else if (isHovered) {
                g2.setColor(new Color(255,255,255,200));
            }
            else {
                g2.setColor(Color.WHITE);
            }

            g2.fillRoundRect(0,0,w,h,arc,arc);

            /*
            --------------------------------
            Left Stripe
            --------------------------------
            */

            Color stripeColor =
                    isEvaluated
                            ? highlight
                            : (isSelected()
                            ? ORANGE_BASED
                            : (isHovered ? ORANGE_HOVER : BORDER_NORMAL));

            g2.setColor(stripeColor);
            g2.fillRoundRect(12,12,8,h-24,4,4);

            /*
            --------------------------------
            Border
            --------------------------------
            */

            g2.setStroke(
                    new BasicStroke(
                            (isSelected() || isHovered || isEvaluated) ? 2f : 1f
                    )
            );

            g2.setColor(
                    isEvaluated
                            ? highlight
                            : (isSelected() || isHovered
                            ? ORANGE_BASED
                            : BORDER_NORMAL)
            );

            g2.drawRoundRect(0,0,w,h,arc,arc);

            /*
            --------------------------------
            Styled Text using Renderer
            --------------------------------
            */

            renderer.setGraphics2D(g2);
            renderer.applyStyle(content.style());

            FontMetrics fm = g2.getFontMetrics();
            int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(INK_DARK);
            g2.drawString(content.text(),45,textY);

            /*
            --------------------------------
            Result Icon (Correct / Wrong)
            --------------------------------
            */

            if (isEvaluated) {

                g2.setFont(new Font("Segoe UI Symbol",Font.PLAIN,14));
                
                if (highlight == SUCCESS_GREEN) {
                    g2.setColor(SUCCESS_GREEN);
                    g2.drawString("✔", w - 30, textY);
                }
                else if (highlight == SCORCH_RED && isSelected()) {
                    g2.setColor(SCORCH_RED);
                    g2.drawString("✖", w - 30, textY);
                }
            }

            g2.dispose();
        }
    }

    public EvaulationView getEvaulationView() {
        return this.evaluationView;
    }

    public boolean isCorrect() { return this.isCorrect; }
}