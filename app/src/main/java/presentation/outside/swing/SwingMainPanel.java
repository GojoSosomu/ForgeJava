package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static presentation.outside.library.LibraryOfColor.*;
import static presentation.outside.library.LibraryOfProjectInfo.*;

public final class SwingMainPanel extends JPanel {
    private JButton startButton;
    private JButton settingsButton;
    private JButton quitButton;

    public SwingMainPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());
        add(createTitlePanel());
        add(Box.createRigidArea(new Dimension(0, 80)));
        add(createButtonPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel(PROJECT_NAME) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, GLOW_YELLOW, 0, getHeight(), ORANGE_BASED);
                g2d.setPaint(gp);
                FontMetrics fm = g2d.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent();
                g2d.setFont(getFont());
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        mainTitle.setMaximumSize(new Dimension(600, 90));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel(QOUTE);
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(new Color(180, 190, 210, 180));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(mainTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(tagline);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = createModernButton("START LESSON", ORANGE_BASED);
        settingsButton = createModernButton("CUSTOMIZE INTERFACE", COOL_GRAY);
        quitButton = createModernButton("EXIT APPLICATION", SCORCH_RED);

        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(settingsButton);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(quitButton);

        return panel;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setForeground(TEXT_ON_WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 22));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color drawColor = isHovered ? baseColor.brighter() : baseColor;
                
                g2d.setColor(drawColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.setColor(getForeground());
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMinimumSize(new Dimension(350, 70));
        button.setPreferredSize(new Dimension(350, 70));
        button.setMaximumSize(new Dimension(400, 80));
        return button;
    }

    public JButton getStartButton() { return startButton; }
    public JButton getSettingsButton() { return settingsButton; }
    public JButton getQuitButton() { return quitButton; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, DARK_BLUE_BASE, 0, getHeight(), DARK_BLUE_DEEP);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        super.paintComponent(g);
    }
}