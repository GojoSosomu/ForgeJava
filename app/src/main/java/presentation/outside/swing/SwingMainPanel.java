package presentation.outside.swing;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static presentation.outside.library.LibraryOfColor.*;
import static presentation.outside.library.LibraryOfProjectInfo.*;

public final class SwingMainPanel extends JPanel {
    private JButton startButton, progressionButton, quitButton, logoutButton, creditButton;
    private JLabel nameLabel;

    public SwingMainPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));

        menuContainer.add(Box.createRigidArea(new Dimension(0, 0)));
        menuContainer.add(createTitlePanel());
        menuContainer.add(Box.createRigidArea(new Dimension(0, 80)));
        menuContainer.add(createButtonPanel());

        menuContainer.add(Box.createVerticalGlue()); 
        menuContainer.add(Box.createVerticalGlue()); 

        add(createProfileHeader(), BorderLayout.NORTH);
        add(menuContainer, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setupGestureListeners();
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        footer.setOpaque(false);
        
        logoutButton = createModernButton("LOGOUT", DEEP_SLATE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(120, 45));
        logoutButton.setMinimumSize(new Dimension(120, 45));
        logoutButton.setMaximumSize(new Dimension(120, 45));
        
        creditButton = createModernButton("CREDIT", GLASS_WHITE);
        creditButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creditButton.setPreferredSize(new Dimension(120, 45));
        creditButton.setMinimumSize(new Dimension(120, 45));
        creditButton.setMaximumSize(new Dimension(120, 45));

        footer.add(logoutButton);
        footer.add(creditButton);
        return footer;
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
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        tagline.setForeground(new Color(180, 190, 210, 180));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(mainTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(tagline);

        return panel;
    }

    public JPanel createProfileHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        headerPanel.setOpaque(false);

        JPanel gridBagPanel = new JPanel(new GridBagLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int calculatedWidth = d.width; 
                return new Dimension(Math.min(300, calculatedWidth), 40);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(withAlpha(GLASS_WHITE, 40)); 
                g2d.fill(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2d.dispose();
            }
        };
        gridBagPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        nameLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                FontMetrics fm = g2d.getFontMetrics(getFont());
                Rectangle viewR = new Rectangle(0, 0, getWidth(), getHeight());
                Rectangle iconR = new Rectangle();
                Rectangle textR = new Rectangle();

                String clippedText = SwingUtilities.layoutCompoundLabel(
                    this, fm, getText(), null, 
                    getVerticalAlignment(), getHorizontalAlignment(),
                    getVerticalTextPosition(), getHorizontalTextPosition(),
                    viewR, iconR, textR, 0
                );

                GradientPaint gp = new GradientPaint(0, 0, GLOW_YELLOW, 0, getHeight(), ORANGE_BASED);
                g2d.setPaint(gp);
                g2d.setFont(getFont());
                
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(clippedText, 0, y);
                g2d.dispose();
            }
        };
        nameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));

        gbCoord(gbc, 0, 0);
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.weightx = 1.0; 
        gbc.insets = new Insets(0, 8, 0, 14); 
        gridBagPanel.add(nameLabel, gbc);

        headerPanel.add(gridBagPanel);
        return headerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = createModernButton("START LESSON", ORANGE_BASED);
        progressionButton = createModernButton("VIEW PROGRESS", COOL_GRAY);
        quitButton = createModernButton("EXIT APPLICATION", SCORCH_RED);

        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(progressionButton);
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
        button.setFocusable(false);
        return button;
    }

    private void gbCoord(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
    }

    public JButton getStartButton() { return startButton; }
    public JButton getProgressionButton() { return progressionButton; }
    public JButton getQuitButton() { return quitButton; }
    public JButton getLogoutButton() { return logoutButton; }
    public JButton getCreditButton() { return creditButton; }

    public JLabel getNameLabel() { return nameLabel; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, DARK_BLUE_BASE, 0, getHeight(), DARK_BLUE_DEEP);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        super.paintComponent(g);
    }

    private void setupGestureListeners() {
        KeyAdapter ka = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) startButton.doClick();
                else if(e.getKeyCode() == KeyEvent.VK_P) progressionButton.doClick();
                else if(e.getKeyCode() == KeyEvent.VK_Q) quitButton.doClick();
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) logoutButton.doClick();
            }
        };

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) { requestFocusInWindow(); }
            @Override public void ancestorRemoved(AncestorEvent e) {}
            @Override public void ancestorMoved(AncestorEvent e) {}
        });

        addKeyListener(ka);
    }
}