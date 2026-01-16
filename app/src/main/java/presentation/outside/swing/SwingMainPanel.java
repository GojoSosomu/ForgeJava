package presentation.outside.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class SwingMainPanel extends JPanel {

    private final Color DEEP_BLUE = new Color(15, 25, 45);
    private final Color FORGE_ORANGE = new Color(230, 126, 34);
    private final Color GLOW_YELLOW = new Color(241, 196, 15);
    private final Color TEXT_WHITE = new Color(245, 245, 245);

    private JButton startButton;
    private JButton settingsButton;
    private JButton quitButton;

    public SwingMainPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(50, 0, 50, 0));

        add(Box.createVerticalGlue());
        add(createTitlePanel());
        add(Box.createRigidArea(new Dimension(0, 60)));
        add(createButtonPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel("FORGE JAVA") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, GLOW_YELLOW, 0, getHeight(), FORGE_ORANGE);
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
        mainTitle.setPreferredSize(new Dimension(400, 60));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Where freedom is forged, structure helps you grow");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        tagline.setForeground(new Color(180, 190, 210));
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

        startButton = createStyledButton("LESSON", FORGE_ORANGE);
        settingsButton = createStyledButton("CUSTOMIZE", new Color(44, 62, 80));
        quitButton = createStyledButton("EXIT", new Color(192, 57, 43));

        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(settingsButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(quitButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
                }
                
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };

        button.setPreferredSize(new Dimension(220, 45));
        button.setMaximumSize(new Dimension(220, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(TEXT_WHITE);
        
        return button;
    }

    public JButton getStartButton() { return startButton; }
    public JButton getSettingsButton() { return settingsButton; }
    public JButton getQuitButton() { return quitButton; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, DEEP_BLUE, 0, getHeight(), Color.BLACK);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}