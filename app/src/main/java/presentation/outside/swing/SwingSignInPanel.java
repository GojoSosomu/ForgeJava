package presentation.outside.swing;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import presentation.outside.launcher.SwingLauncher;
import presentation.outside.library.LibraryOfColor;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingSignInPanel extends JPanel {
    private SwingLauncher swingLauncher;

    private JButton signInButton;
    private JButton switchToLoginButton;
    private JLabel errorLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JToggleButton passwordToggle;
    private JToggleButton confirmPasswordToggle;

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
        gbc.insets = new Insets(0, 10, 10, 10);
        add(titleLabel, gbc);

        errorLabel = new JLabel(" "); 
        errorLabel.setForeground(LibraryOfColor.SCORCH_RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 5, 10);
        add(errorLabel, gbc);

        gbc.gridwidth = 1;

        add(createLabel("Username"), setGbc(0, 2, gbc));
        usernameField = createStyledTextField(15);
        add(usernameField, setGbc(1, 2, gbc));

        add(createLabel("Password"), setGbc(0, 3, gbc));
        passwordField = createStyledPasswordField(15);
        passwordToggle = createVisibilityButton(passwordField);
        add(createFieldWrapper(passwordField, passwordToggle), setGbc(1, 3, gbc));

        add(createLabel("Confirm Password"), setGbc(0, 4, gbc));
        confirmPasswordField = createStyledPasswordField(15);
        confirmPasswordToggle = createVisibilityButton(confirmPasswordField);
        add(createFieldWrapper(confirmPasswordField, confirmPasswordToggle), setGbc(1, 4, gbc));

        signInButton = createModernButton("Sign In", ORANGE_BASED);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 10, 10, 10);
        add(signInButton, gbc);

        switchToLoginButton = new JButton("Already have an account? Login");
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setFocusPainted(false);
        switchToLoginButton.setForeground(COOL_GRAY);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(switchToLoginButton, gbc);

        setUpListeners();
    }

    private JLayeredPane createFieldWrapper(JPasswordField field, JToggleButton btn) {
        JLayeredPane layer = new JLayeredPane();
        int w = field.getPreferredSize().width;
        int h = field.getPreferredSize().height;
        layer.setPreferredSize(new Dimension(w, h));

        field.setBounds(0, 0, w, h);
        int btnSize = 70;
        btn.setBounds(w - btnSize + 20, (h - btnSize) / 2 - 2, btnSize, btnSize);

        layer.add(field, JLayeredPane.DEFAULT_LAYER);
        layer.add(btn, JLayeredPane.POPUP_LAYER);
        return layer;
    }

    private JToggleButton createVisibilityButton(JPasswordField field) {
        JToggleButton btn = new JToggleButton("👁");
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(ORANGE_BASED);
        btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));

        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                field.setEchoChar((char) 0);
                btn.setForeground(GLOW_YELLOW);
            } else {
                field.setEchoChar('•');
                btn.setForeground(ORANGE_BASED);
            }
            field.requestFocusInWindow();
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
        signInButton.addActionListener(e -> signIn());
        
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                usernameField.requestFocusInWindow(); 
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
            BorderFactory.createEmptyBorder(5, 5, 5, 35)
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
