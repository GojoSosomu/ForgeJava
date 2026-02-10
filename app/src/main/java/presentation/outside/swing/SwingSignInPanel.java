package presentation.outside.swing;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import presentation.outside.launcher.SwingLauncher;
import presentation.outside.library.LibraryOfColor;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingSignInPanel extends JPanel {
    private SwingLauncher swingLauncher;

    private JButton signInButton;
    private JButton switchToLoginButton;
    private JLabel errorLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SwingSignInPanel(SwingLauncher swingLauncher) {
        setBackground(DARK_BLUE_BASE);
        setLayout(new GridBagLayout());

        this.swingLauncher = swingLauncher;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ORANGE_BASED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 20, 10);
        add(titleLabel, gbc);

        errorLabel = new JLabel(" "); 
        errorLabel.setForeground(LibraryOfColor.SCORCH_RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(errorLabel, gbc);

        gbc.gridwidth = 1;

        add(createLabel("Username"), setGbc(0, 2, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 2, gbc));

        add(createLabel("Password"), setGbc(0, 3, gbc));
        passwordField = createStyledPasswordField(15);
        add(passwordField, setGbc(1, 3, gbc));

        add(createLabel("Confirm Password"), setGbc(0, 4, gbc));
        confirmPasswordField = createStyledPasswordField(15);
        add(confirmPasswordField, setGbc(1, 4, gbc));

        signInButton = new JButton("Sign In");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        add(signInButton, gbc);

        switchToLoginButton = new JButton("Already have an account? Login");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(switchToLoginButton, gbc);

        setUpListeners();
    }

    private void setUpListeners() {
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signIn();
                }
            }
        };

        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        confirmPasswordField.addKeyListener(enterListener);
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                requestFocusInWindow(); 
            }
            @Override public void ancestorRemoved(AncestorEvent e) {}
            @Override public void ancestorMoved(AncestorEvent e) {}
        });
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        errorLabel.setText(" ");
    }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }

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
        field.setForeground(Color.WHITE);
        field.setCaretColor(ORANGE_BASED);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ORANGE_BASED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void signIn() {
        if (getUsername().isEmpty() || getPassword().isEmpty() || getConfirmPassword().isEmpty()) {
            updateErrorLabelText("Please complete all required fields.");
            return;
        }
        swingLauncher.signInSuccessToMainPanel(this);
    }

    public JButton getSignInButton() { return signInButton; }
    public JButton getSwitchToLoginButton() { return switchToLoginButton; }
    public JLabel getErrorLabel() { return errorLabel; }

    public void updateErrorLabelText(String text) {
        errorLabel.setText(text);
        this.revalidate(); 
        this.repaint();
    }

    public void start() {
        usernameField.requestFocusInWindow();
    }

    private GridBagConstraints setGbc(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        return gbc;
    }
}