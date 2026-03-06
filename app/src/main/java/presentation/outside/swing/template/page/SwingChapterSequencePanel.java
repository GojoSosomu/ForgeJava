package presentation.outside.swing.template.page;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import core.model.view.chapter.ChapterSequenceView;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingChapterSequencePanel extends JPanel implements ActionListener {

    private JButton lessonButton, activityButton;
    private JLabel lessonLabel, activityLabel;
    private JScrollPane lessonScrollPane, activityScrollPane;
    private JPanel lessonPanel, activityPanel;
    
    private JPanel contentPanel;
    private JPanel completedPanel;
    private JButton completedButton;
    
    private JButton backButton;

    private final int LEFT_MARGIN = 30;
    private final int PANEL_WIDTH = 1120;
    private final int MAX_VISIBLE_HEIGHT = 235;

    public SwingChapterSequencePanel(ChapterSequenceView chapterSequenceView, Runnable onBack, Runnable onComplete) {        
        setLayout(new BorderLayout());
        setBackground(PAGE_BASE); // Matching the background to your other panels

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));

        backButton = new JButton("← BACK");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(INK_MEDIUM);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> onBack.run());

        headerPanel.add(backButton);

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);

        lessonLabel = createTitleLabel("LESSONS");
        lessonButton = createArrowButton();
        lessonPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        lessonPanel.setOpaque(false);
        
        for(var entry : chapterSequenceView.lessonViews().entrySet()) {
            lessonPanel.add(createItemButton(entry.getValue().id()));
        }
        lessonScrollPane = createCustomScrollPane(lessonPanel);

        activityLabel = createTitleLabel("ACTIVITIES");
        activityButton = createArrowButton();
        activityPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        activityPanel.setOpaque(false);

        for(var entry : chapterSequenceView.activityViews().entrySet()) {
            activityPanel.add(createItemButton(entry.getValue().id()));
        }
        activityScrollPane = createCustomScrollPane(activityPanel);

        contentPanel.add(lessonLabel); 
        contentPanel.add(lessonButton); 
        contentPanel.add(lessonScrollPane);
        contentPanel.add(activityLabel); 
        contentPanel.add(activityButton); 
        contentPanel.add(activityScrollPane);

        completedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        completedPanel.setOpaque(false);
        completedPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        completedButton = new JButton("MARK AS COMPLETED");
        completedButton.setPreferredSize(new Dimension(240, 50));
        completedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        completedButton.setBackground(ORANGE_BASED);
        completedButton.setForeground(Color.WHITE);
        completedButton.setFocusPainted(false);
        completedButton.setBorder(BorderFactory.createEmptyBorder());
        setCompletedButtonVisible(false);

        completedButton.addActionListener(e -> onComplete.run());
        completedPanel.add(completedButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(completedPanel, BorderLayout.SOUTH);

        relayout();
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(INK_MEDIUM);
        label.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_NORMAL)); 
        return label;
    }

    private JButton createArrowButton() {
        JButton btn = new JButton("↓");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setForeground(ORANGE_BASED);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    private JButton createItemButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(INK_DARK);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_NORMAL, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return btn;
    }

    private JScrollPane createCustomScrollPane(JPanel content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null); 
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        
        // Match the Modern ScrollBar logic from Intro/Outro
        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.getVerticalScrollBar().setUnitIncrement(14);
        sp.getVerticalScrollBar().setOpaque(false);
        
        sp.setVisible(false);
        return sp;
    }

    private void relayout() {
        int currentY = 10;

        lessonLabel.setBounds(LEFT_MARGIN, currentY, 200, 35);
        lessonButton.setBounds(LEFT_MARGIN + 210, currentY, 45, 30);
        currentY += 45;

        if (lessonScrollPane.isVisible()) {
            int preferredHeight = lessonPanel.getPreferredSize().height;
            int displayHeight = Math.min(preferredHeight, MAX_VISIBLE_HEIGHT);
            lessonScrollPane.setBounds(LEFT_MARGIN + 10, currentY, PANEL_WIDTH, displayHeight);
            currentY += displayHeight + 20;
        }

        activityLabel.setBounds(LEFT_MARGIN, currentY, 200, 35);
        activityButton.setBounds(LEFT_MARGIN + 210, currentY, 45, 30);
        currentY += 45;

        if (activityScrollPane.isVisible()) {
            int preferredHeight = activityPanel.getPreferredSize().height;
            int displayHeight = Math.min(preferredHeight, MAX_VISIBLE_HEIGHT);
            activityScrollPane.setBounds(LEFT_MARGIN + 10, currentY, PANEL_WIDTH, displayHeight);
            currentY += displayHeight + 20;
        }

        contentPanel.setPreferredSize(new Dimension(getWidth(), currentY + 50));
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lessonButton) {
            lessonScrollPane.setVisible(!lessonScrollPane.isVisible());
            lessonButton.setText(lessonScrollPane.isVisible() ? "↑" : "↓");
        } else if (e.getSource() == activityButton) {
            activityScrollPane.setVisible(!activityScrollPane.isVisible());
            activityButton.setText(activityScrollPane.isVisible() ? "↑" : "↓");
        }
        relayout();
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

    public void setCompletedButtonVisible(boolean visible) {
        completedButton.setVisible(visible);
        revalidate();
        repaint();
    }

    public JButton getCompletedButton() {
        return completedButton;
    }
}