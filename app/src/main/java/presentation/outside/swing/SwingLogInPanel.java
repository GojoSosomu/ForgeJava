package presentation.outside.swing;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import presentation.outside.launcher.SwingLauncher;
import presentation.outside.library.LibraryOfColor;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingLogInPanel extends JPanel {
    private SwingLauncher swingLauncher;

    private JButton logInButton;
    private JButton switchToSignInButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
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
        gbc.insets = new Insets(0, 10, 30, 10);
        add(titleLabel, gbc);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(LibraryOfColor.SCORCH_RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorLabel.setPreferredSize(new Dimension(200, 20));
        add(errorLabel, setGbc(0, 1, gbc));

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        add(createLabel("Username"), setGbc(0, 2, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 2, gbc));

        add(createLabel("Password"), setGbc(0, 3, gbc));
        passwordField = createStyledPasswordField(15);
        add(passwordField, setGbc(1, 3, gbc));

        logInButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(logInButton, gbc);

        switchToSignInButton = new JButton("Don't have an account? Sign In");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(switchToSignInButton, gbc);

        setUpListeners();

    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
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
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
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
