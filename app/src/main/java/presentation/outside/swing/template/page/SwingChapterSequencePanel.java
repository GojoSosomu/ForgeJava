package presentation.outside.swing.template.page;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import core.model.view.chapter.ChapterSequenceView;
import presentation.outside.launcher.SwingLauncher;
import presentation.service.ChapterService;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingChapterSequencePanel extends JPanel implements ActionListener {
    private ChapterService service;
    private String currentChapter;

    private JButton lessonButton, activityButton;
    private JLabel lessonLabel, activityLabel, nextLabel;
    private JScrollPane lessonScrollPane, activityScrollPane;
    private JPanel lessonPanel, activityPanel;
    
    private JPanel contentPanel;
    private JPanel completedPanel;
    private JButton completedButton;
    
    private JButton backButton;

    private final int LEFT_MARGIN = 30;
    private final int PANEL_WIDTH = 1120;
    private final int MAX_VISIBLE_HEIGHT = 235;

    private ChapterSequenceView chapterSequenceView;
    private int chapterNumber;

    public SwingChapterSequencePanel(
        ChapterService service,
        SwingLauncher launcher,
        String id,
        ChapterSequenceView chapterSequenceView, 
        Runnable onBack, 
        Runnable onComplete
    ) {
        this.service = service;
        this.currentChapter = id;
        this.chapterNumber = service.parseId(id);
        this.chapterSequenceView = chapterSequenceView;
        
        setLayout(new BorderLayout());
        setBackground(PAGE_BASE); // Matching the background to your other panels

        JPanel headerPanel = new JPanel(new GridBagLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20)); // Added right padding for balance

        GridBagConstraints gbc = new GridBagConstraints();

        backButton = new JButton("← BACK");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(INK_MEDIUM);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> onBack.run());

        gbc.gridx = 0;             // Column 0
        gbc.weightx = 0.0;         // Don't grow
        gbc.anchor = GridBagConstraints.WEST; // Stick to the left
        headerPanel.add(backButton, gbc);

        nextLabel = new JLabel("FIRSTLY GO: ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(0, 0, INK_MEDIUM, 0, getHeight(), INK_DARK);
                g2d.setPaint(gp);
                
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                
                // MATH: Center the text inside the label component
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent();
                
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        nextLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32)); 

        gbc.gridx = 1;             // Column 1
        gbc.weightx = 1.0;         // Take up all available horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expand to fill the center
        gbc.anchor = GridBagConstraints.CENTER;   // Stay in the middle
        headerPanel.add(nextLabel, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(Box.createHorizontalStrut(backButton.getPreferredSize().width), gbc);

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);

        lessonLabel = createTitleLabel("LESSONS");
        lessonButton = createArrowButton();
        lessonPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        lessonPanel.setOpaque(false);
        
        for(var entry : chapterSequenceView.lessonViews().entrySet()) {
            JButton btn = createItemButton(entry.getValue().id());
            btn.addActionListener(e -> launcher.startLesson(entry.getValue().id()));

            lessonPanel.add(btn);
        }
        lessonScrollPane = createCustomScrollPane(lessonPanel);

        activityLabel = createTitleLabel("ACTIVITIES");
        activityButton = createArrowButton();
        activityPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        activityPanel.setOpaque(false);

        for(var entry : chapterSequenceView.activityViews().entrySet()) {
            JButton btn = createItemButton(entry.getValue().id());
            btn.addActionListener(e -> launcher.startActivity(entry.getValue().id()));

            activityPanel.add(btn);
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
        boolean isLocked = service.isItemLocked(currentChapter, text);
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                // Initial setup
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.LEFT);
                
                // Add Hover Logic
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { if(!isLocked) { isHovered = true; repaint(); } }
                    public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Draw the Background (The "Card")
                if (isLocked) {
                    g2.setColor(new Color(240, 240, 240)); // Muted Gray for locked
                } else if (isHovered) {
                    g2.setColor(Color.WHITE); // Brighter on hover
                } else {
                    g2.setColor(new Color(252, 252, 252)); // Standard clean white
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // 2. Draw the "Status Stripe" (The professional touch)
                // An orange stripe on the left if unlocked, gray if locked
                g2.setColor(isLocked ? LOCKED_GRAY : (isHovered ? ORANGE_HOVER : ORANGE_BASED));
                g2.fillRoundRect(8, 10, 4, getHeight() - 20, 2, 2);

                // 3. Draw the Border
                g2.setColor(isLocked ? new Color(220, 220, 220) : (isHovered ? ORANGE_BASED : BORDER_NORMAL));
                g2.setStroke(new BasicStroke(isHovered ? 1.5f : 1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // 4. Draw the Text
                g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
                g2.setColor(isLocked ? LOCKED_GRAY : INK_DARK);
                
                FontMetrics fm = g2.getFontMetrics();
                int textX = 30; // Move text to the right of the stripe
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2.drawString(getText(), textX, textY);

                // 5. Draw "Lock" icon if needed
                if (isLocked) {
                    g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
                    g2.drawString("🔒", getWidth() - 30, textY);
                }

                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(PANEL_WIDTH - 20, 50)); // Thicker, more touchable
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

    public void updated() {
        for(Component component : lessonPanel.getComponents()) {
            if(component instanceof JButton) {
                JButton buttomItems = ((JButton)component);
                buttomItems.setEnabled(!service.isItemLocked(currentChapter, buttomItems.getText()));
            }
        }
        for(Component component : activityPanel.getComponents()) {
            if(component instanceof JButton) {
                JButton buttomItems = ((JButton)component);
                buttomItems.setEnabled(!service.isItemLocked(currentChapter, buttomItems.getText()));
            }
        }

        if(service.getCurrentChapterIndex() > chapterNumber) {
            nextLabel.setText("CHAPTER COMPLETED! Proceed Chapter " + service.getCurrentChapterIndex());
            setCompletedButtonVisible(true);
        } else if (service.getCurrentSequenceIndex() > chapterSequenceView.sequence().size() - 1) {
            nextLabel.setText("You completed the CHAPTER + " + chapterNumber +  "! Proceed to Chapter " + service.getCurrentChapterIndex());
            setCompletedButtonVisible(true);
        } else if(service.getCurrentSequenceIndex() == 0)
            nextLabel.setText(
                "FIRSTLY GO: " + 
                chapterSequenceView.sequence().get(service.getCurrentSequenceIndex())
            );
        else if(service.getCurrentSequenceIndex() == chapterSequenceView.sequence().size() - 1)
            nextLabel.setText(
                "LASTLY GO: " + 
                chapterSequenceView.sequence().get(service.getCurrentSequenceIndex())
            );
        else
            nextLabel.setText(
                "NEXT GO: " + 
                chapterSequenceView.sequence().get(service.getCurrentSequenceIndex())
            );
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