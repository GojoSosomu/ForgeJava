package presentation.outside.swing.template.activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import core.model.view.progress.info.ScoreView;
import presentation.outside.channel.OutsideChannel;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingActivityResultPanel extends JPanel implements OutsideChannel<ScoreView> {
    private final JLabel scoreLabel = new JLabel();
    private final JLabel statusLabel = new JLabel();
    private final Runnable onDismiss;
    
    private Color resultColor = ORANGE_BASED; // Dynamic color based on score

    public SwingActivityResultPanel(Runnable onDismiss) {
        this.onDismiss = onDismiss;
        
        setLayout(new BorderLayout());
        setBackground(DARK_BLUE_BASE);
        setFocusable(true); // REQUIRED FOR KEY LISTENER

        // 1. Central Result Card
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        
        JPanel resultCard = createResultCard();
        cardWrapper.add(resultCard);
        
        add(cardWrapper, BorderLayout.CENTER);

        // 2. Footer Instruction
        JLabel hint = new JLabel("PRESS [ENTER] TO RETURN TO CENTRAL COMMAND", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(withAlpha(TEXT_OFF_WHITE, 100));
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        add(hint, BorderLayout.SOUTH);

        setupKeyboardNavigation();
    }

    private JPanel createResultCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background (Clean Card)
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Industrial Stripe (Colored by result)
                g2.setColor(resultColor);
                g2.fillRoundRect(15, 20, 10, getHeight() - 40, 5, 5);
                
                // Subtle Border
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(withAlpha(resultColor, 50));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                g2.dispose();
            }
        };
        
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(600, 350));
        card.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 40, 5, 20);

        // Evaluation Header
        JLabel header = new JLabel("INDUSTRIAL EVALUATION COMPLETE");
        header.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        header.setForeground(COOL_GRAY);
        card.add(header, gbc);

        // Main Score
        scoreLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 92));
        scoreLabel.setForeground(INK_DARK);
        card.add(scoreLabel, gbc);

        // Status
        statusLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 24));
        card.add(statusLabel, gbc);

        return card;
    }

    private void setupKeyboardNavigation() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onDismiss.run();
                }
            }
        });
        
        // Ensure the panel gets focus so ENTER works immediately
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                requestFocusInWindow();
            }
        });
    }

    @Override
    public void render(ScoreView view) {
        // 1. Calculate the color based on performance
        double percent = (double) view.score() / view.total();
        if (percent >= 1.0) resultColor = SUCCESS_GREEN;
        else if (percent >= 0.75) resultColor = ORANGE_BASED;
        else resultColor = SCORCH_RED;

        // 2. Update the Text
        scoreLabel.setText(view.score() + "/" + view.total());
        statusLabel.setText(view.status());
        statusLabel.setForeground(resultColor);
        
        // 3. Force focus again for the keyboard
        requestFocusInWindow();
        repaint();
    }
}