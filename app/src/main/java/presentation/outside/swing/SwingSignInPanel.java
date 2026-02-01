package presentation.outside.swing;
import javax.swing.*;

import java.awt.*;

import static presentation.outside.color.LibraryOfColor.*;

public class SwingSignInPanel extends JPanel {
    private JButton signInButton;
    private JButton switchToLoginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SwingSignInPanel() {
        setBackground(DARK_BLUE_BASE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ORANGE_BASED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 25, 10);
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        add(createLabel("Username"), setGbc(0, 1, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 1, gbc));

        add(createLabel("Password"), setGbc(0, 2, gbc));
        passwordField = createStyledPasswordField(15);
        add(passwordField, setGbc(1, 2, gbc));

        add(createLabel("Confirm Password"), setGbc(0, 3, gbc));
        confirmPasswordField = createStyledPasswordField(15);
        add(confirmPasswordField, setGbc(1, 3, gbc));

        signInButton = new JButton("Sign In");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        add(signInButton, gbc);

        switchToLoginButton = new JButton("Already have an account? Login");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(switchToLoginButton, gbc);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
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

    public JButton getSignInButton() { return signInButton; }
    public JButton getSwitchToLoginButton() { return switchToLoginButton; }

    private GridBagConstraints setGbc(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }
}