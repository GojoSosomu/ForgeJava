package presentation.outside.swing.template.activity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import core.model.view.View;
import core.model.view.activity.evaulation.EvaulationView;
import core.model.view.activity.result.QuestionnaireResultView;
import core.model.view.progress.info.ScoreView;
import presentation.outside.interfaces.IActivityResult;
import presentation.outside.launcher.SwingLauncher;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingQuestionnaireResultPanel extends JPanel implements IActivityResult {
    private final JLabel scoreLabel = new JLabel(), statusLabel = new JLabel();
    private final JPanel detailsContainer = new JPanel(); // New container for individual results

    private final Runnable onDismiss;
    private final SwingLauncher launcher;
    private QuestionnaireResultView view;
    
    private Color resultColor = ORANGE_BASED;

    public SwingQuestionnaireResultPanel(Runnable onDismiss, SwingLauncher launcher) {
        this.onDismiss = onDismiss;
        this.launcher = launcher;
        
        setLayout(new BorderLayout());
        setBackground(DARK_BLUE_BASE);
        setFocusable(true);

        // 1. Central Layout
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Add the Score Card
        gbc.insets = new Insets(20, 0, 10, 0);
        mainContent.add(createResultCard(), gbc);

        // Add the Details List (Scrollable)
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainContent.add(createScrollableDetails(), gbc);
        
        add(mainContent, BorderLayout.CENTER);

        // 2. Footer
        JLabel hint = new JLabel("PRESS [ENTER] TO RETURN TO CENTRAL COMMAND", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(withAlpha(TEXT_OFF_WHITE, 600));
        hint.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(hint, BorderLayout.SOUTH);

        setupKeyboardNavigation();
    }

    private JScrollPane createScrollableDetails() {
        detailsContainer.setLayout(new BoxLayout(detailsContainer, BoxLayout.Y_AXIS));
        detailsContainer.setOpaque(false);

        JScrollPane sp = new JScrollPane(detailsContainer);
        sp.setFocusable(false);
        sp.getVerticalScrollBar().setFocusable(false); 
        sp.getViewport().setOpaque(false);
        sp.getViewport().setFocusable(false);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.getVerticalScrollBar().setUnitIncrement(14);
        sp.getVerticalScrollBar().setOpaque(false);
        return sp;
    }

    private JPanel createResultCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(resultColor);
                g2.fillRoundRect(15, 20, 10, getHeight() - 40, 5, 5);
                g2.dispose();
            }
        };
        
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(600, 200));
        card.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        scoreLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 72));
        scoreLabel.setForeground(INK_DARK);
        card.add(scoreLabel, gbc);

        statusLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 20));
        card.add(statusLabel, gbc);

        return card;
    }

    private void setupKeyboardNavigation() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onDismiss.run();
            }
        });
        
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                requestFocusInWindow();
            }
        });
    }

    @Override
    public void setView(View view) { 
        this.view = (QuestionnaireResultView) view; 
    }

    @Override
    public void render(ScoreView scoreView) {
        // 1. Calculate color based on performance
        double percent = (double) scoreView.score() / scoreView.total();
        if (percent >= 1.0) resultColor = SUCCESS_GREEN;
        else if (percent >= 0.75) resultColor = ORANGE_BASED;
        else resultColor = SCORCH_RED;

        // 2. Update Header Text
        scoreLabel.setText(scoreView.score() + "/" + scoreView.total());
        statusLabel.setText(scoreView.status());
        statusLabel.setForeground(resultColor);

        // 3. Update Detailed Breakdown
        detailsContainer.removeAll();
        if (view != null && view.result() != null) {
            int index = 1;
            for (EvaulationView eval : view.result()) {
                detailsContainer.add(createEvaluationRow(index++, eval));
                detailsContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        revalidate();
        repaint();
        requestFocusInWindow();
    }

    private JPanel createEvaluationRow(int index, EvaulationView eval) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(550, 50));
        row.setBorder(new EmptyBorder(5, 50, 5, 50));

        String icon = eval.isUserCorrect() ? "✔" : "✖"; // Note: used slightly different Unicode
        Color color = eval.isUserCorrect() ? SUCCESS_GREEN : SCORCH_RED;

        JLabel statusIcon = new JLabel(icon + "  Question " + index);
        statusIcon.setFont(new Font("Dialog", Font.BOLD, 18)); 
        statusIcon.setForeground(color);

        row.add(statusIcon, BorderLayout.WEST);

        if (!eval.isUserCorrect()) {
            JLabel answerLabel = new JLabel("Correct: " + eval.rightAnswer());
            answerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            answerLabel.setForeground(withAlpha(TEXT_OFF_WHITE, 700));
            row.add(answerLabel, BorderLayout.EAST);
        }

        return row;
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() { this.thumbColor = withAlpha(ORANGE_BASED, 80); }
        @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
            g2.dispose();
        }
        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }

    @Override
    public void showResult() {
        launcher.switchPanel(this);
    }
}