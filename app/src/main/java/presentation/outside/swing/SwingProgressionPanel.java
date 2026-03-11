package presentation.outside.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import core.model.view.progress.info.ProgressInfo;
import static presentation.outside.library.LibraryOfColor.*;

public class SwingProgressionPanel extends JPanel {
    private final JButton backButton;

    public SwingProgressionPanel(ProgressInfo info, Runnable onBack) {
        setLayout(new BorderLayout());
        setBackground(DARK_BLUE_BASE);

        // 1. Header (Metropolis Signature)
        JLabel title = new JLabel("INDUSTRIAL PROGRESS REPORT", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        title.setForeground(ORANGE_BASED);
        title.setBorder(new EmptyBorder(40, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // 2. The List Container (Center Stack)
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);
        // Added balanced padding for the overall scroll area
        listContainer.setBorder(new EmptyBorder(0, 100, 50, 100));

        // --- Build Category: CHAPTERS ---
        if (!info.completedChapters().isEmpty()) {
            addCategoryHeader(listContainer, "PROCESSED CHAPTERS -> " + (info.currentChapter() >= info.completedChapters().size() ? "FINISHED! CHAPTER COMING SOON" : "CURRENT CHAPTER: " + info.currentChapter()));
            for (String chapter : info.completedChapters()) {
                listContainer.add(createProgressCard(chapter, "CHAPTER MASTERED ✓", SUCCESS_GREEN));
                listContainer.add(Box.createRigidArea(new Dimension(0, 12))); // Gap between cards
            }
        }

        // --- Build Category: LESSONS ---
        if (!info.completedLessons().isEmpty()) {
            addCategoryHeader(listContainer, "MASTERED LESSON UNITS");
            for (String lesson : info.completedLessons()) {
                listContainer.add(createProgressCard(lesson, "UNIT PROCESSED", CYAN_NEON));
                listContainer.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        // --- Build Category: ACTIVITIES ---
        if (!info.completedActivities().isEmpty()) {
            addCategoryHeader(listContainer, "ACTIVITY EVALUATIONS");
            info.completedActivities().forEach((id, scoreView) -> {
                String detail = "RESULT: " + scoreView.score() + "/" + scoreView.total() + " [" + scoreView.status() + "]";
                Color statusColor = scoreView.status().contains("FAILED") ? SCORCH_RED : GLOW_YELLOW;
                listContainer.add(createProgressCard(id, detail, statusColor));
                listContainer.add(Box.createRigidArea(new Dimension(0, 12)));
            });
        }

        // Use JScrollPane with invisible borders
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer (Back Button)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centered back button
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 40, 0));
        
        this.backButton = createModernBackButton("← RETURN TO CENTRAL COMMAND", onBack);
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);
    }

    private void addCategoryHeader(JPanel container, String title) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        label.setForeground(COOL_GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // FORCE CENTER
        label.setBorder(new EmptyBorder(40, 0, 15, 0)); // Top margin for new section
        container.add(label);
    }

    private JPanel createProgressCard(String id, String detail, Color stripeColor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card Body
                g2.setColor(withAlpha(DEEP_SLATE, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                
                // Consistent Stripe (Industrial Seal)
                g2.setColor(stripeColor);
                g2.fillRoundRect(0, 0, 8, getHeight(), 18, 0); 
                
                // Subtle Border
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(withAlpha(BORDER_NORMAL, 40));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.CENTER_ALIGNMENT); // FORCE CENTER
        
        // Rigid Size for cleaner look
        card.setMinimumSize(new Dimension(600, 65));
        card.setPreferredSize(new Dimension(800, 65));
        card.setMaximumSize(new Dimension(900, 65));
        card.setBorder(new EmptyBorder(0, 30, 0, 30));

        JLabel idLabel = new JLabel(id);
        idLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        idLabel.setForeground(TEXT_ON_WHITE);

        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 14));
        detailLabel.setForeground(stripeColor);

        card.add(idLabel, BorderLayout.WEST);
        card.add(detailLabel, BorderLayout.EAST);

        return card;
    }

    private JButton createModernBackButton(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(ORANGE_BASED);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createLineBorder(ORANGE_BASED, 2));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(300, 50));
        b.addActionListener(e -> action.run());
        return b;
    }
}