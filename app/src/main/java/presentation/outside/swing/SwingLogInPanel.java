package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import presentation.outside.launcher.SwingLauncher;
import presentation.outside.library.LibraryOfColor;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingLogInPanel extends JPanel {
    private SwingLauncher swingLauncher;

    private JButton logInButton;
    private JButton switchToSignInButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton toggleButton;
    private JLabel errorLabel;

    public SwingLogInPanel(
        SwingLauncher swingLauncher
    ) {
        setBackground(DARK_BLUE_BASE);
        setLayout(new GridBagLayout());

        this.swingLauncher = swingLauncher;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ORANGE_BASED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(titleLabel, gbc);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(LibraryOfColor.SCORCH_RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorLabel.setPreferredSize(new Dimension(200, 20));
        add(errorLabel, setGbc(0, 1, gbc));

        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 5, 10);

        add(createLabel("Username"), setGbc(0, 2, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 2, gbc));

        add(createLabel("Password"), setGbc(0, 3, gbc));
        
        passwordField = createStyledPasswordField(15);
        toggleButton = createVisibilityButton();

        JLayeredPane passLayer = new JLayeredPane();
        int fieldWidth = passwordField.getPreferredSize().width;
        int fieldHeight = passwordField.getPreferredSize().height;
        passLayer.setPreferredSize(new Dimension(fieldWidth, fieldHeight));

        passwordField.setBounds(0, 0, fieldWidth, fieldHeight);
        int btnSize = 70;
        toggleButton.setBounds(fieldWidth - btnSize + 20, (fieldHeight - btnSize) / 2 - 2, btnSize, btnSize);

        passLayer.add(passwordField, JLayeredPane.DEFAULT_LAYER);
        passLayer.add(toggleButton, JLayeredPane.POPUP_LAYER);

        add(passLayer, setGbc(1, 3, gbc));

        logInButton = createModernButton("Login", ORANGE_BASED);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(logInButton, gbc);

        switchToSignInButton = new JButton("Don't have an account? Sign In");
        switchToSignInButton.setContentAreaFilled(false);
        switchToSignInButton.setBorderPainted(false);
        switchToSignInButton.setFocusPainted(false);
        switchToSignInButton.setForeground(COOL_GRAY);
        switchToSignInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(switchToSignInButton, gbc);

        setUpListeners();
    }

    private JToggleButton createVisibilityButton() {
        JToggleButton btn = new JToggleButton("👁");
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(ORANGE_BASED);
        btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));

        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                passwordField.setEchoChar((char) 0);
                btn.setForeground(GLOW_YELLOW);
            } else {
                passwordField.setEchoChar('•');
                btn.setForeground(ORANGE_BASED);
            }
            passwordField.requestFocusInWindow();
        });
        return btn;
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
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHovered ? baseColor.brighter() : baseColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(280, 45));
        return button;
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText("");
    }

    public void start() {
        usernameField.requestFocusInWindow();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    private void setUpListeners() {
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    logIn();
                }
            }
        };

        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        logInButton.addActionListener(e -> logIn());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GLOW_YELLOW);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JTextField createStyledTextField(int cols) {
        JTextField field = new JTextField(cols);
        styleInput(field);
        return field;
    }

    private JPasswordField createStyledPasswordField(int cols) {
        JPasswordField field = new JPasswordField(cols);
        styleInput(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setOpaque(false);
        field.setForeground(TEXT_ON_WHITE);
        field.setCaretColor(ORANGE_BASED);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ORANGE_BASED),
            BorderFactory.createEmptyBorder(5, 5, 5, 35)
        ));
    }

    private void logIn() {
        swingLauncher.loginSuccessToMainPanel(this);
    }

    public JButton getLogInButton() { return logInButton; }
    public JButton getSwitchToSignInButton() { return switchToSignInButton; }

    public void updateErrorLabelText(String text) {
        errorLabel.setText(text);
        errorLabel.revalidate();
        errorLabel.repaint();
    }

    private GridBagConstraints setGbc(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        return gbc;
    }
}
