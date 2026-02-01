package presentation.outside.swing;
import javax.swing.*;

import static presentation.outside.color.LibraryOfColor.*;

import java.awt.*;
public class SwingLogInPanel extends JPanel {
    private JButton logInButton;
    private JButton switchToSignInButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SwingLogInPanel() {
        setBackground(DARK_BLUE_BASE);
        setLayout(new GridBagLayout());
        
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

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        add(createLabel("Username"), setGbc(0, 1, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 1, gbc));

        add(createLabel("Password"), setGbc(0, 2, gbc));
        passwordField = createStyledPasswordField(15);
        add(passwordField, setGbc(1, 2, gbc));

        logInButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(logInButton, gbc);

        switchToSignInButton = new JButton("Don't have an account? Sign In");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(switchToSignInButton, gbc);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
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

    public JButton getLogInButton() { return logInButton; }
    public JButton getSwitchToSignInButton() { return switchToSignInButton; }

    private GridBagConstraints setGbc(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        return gbc;
    }
}
