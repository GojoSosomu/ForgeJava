package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import static presentation.outside.library.LibraryOfColor.*;

public class SwingCreditPanel extends JPanel {
    private JButton backButton;

    public SwingCreditPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("PROJECT CREDITS");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setForeground(ORANGE_BASED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font nameFont = new Font("Segoe UI Semibold", Font.PLAIN, 18);
        Font roleFont = new Font("Segoe UI", Font.BOLD, 12);

        centerWrapper.add(Box.createRigidArea(new Dimension(0, 80))); 
        centerWrapper.add(titleLabel);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 40)));

        addCreditEntry(centerWrapper, "LEADER", "Dele Pena", roleFont, nameFont);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        
        addCreditEntry(centerWrapper, "ASSISTANT LOGIC", "Ian Partriaca", roleFont, nameFont);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        
        addCreditEntry(centerWrapper, "LEADING GUI DESIGNER", "Kyle Cristobal", roleFont, nameFont);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        
        addCreditEntry(centerWrapper, "ASSISTANT GUI DESIGNER", "Anghel Ladrera", roleFont, nameFont);

        centerWrapper.add(Box.createVerticalGlue());
        centerWrapper.add(Box.createVerticalGlue());

        add(centerWrapper, BorderLayout.CENTER);
    }


    private void addCreditEntry(JPanel target, String role, String name, Font roleF, Font nameF) {
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(roleF);
        roleLabel.setForeground(COOL_GRAY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameF);
        nameLabel.setForeground(TEXT_ON_WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        target.add(roleLabel);
        target.add(Box.createRigidArea(new Dimension(0, 5)));
        target.add(nameLabel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        header.setOpaque(false);
        
        backButton = createModernButton("BACK", SCORCH_RED);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setMinimumSize(new Dimension(100, 40));
        backButton.setMaximumSize(new Dimension(100, 40));

        header.add(backButton);
        return header;
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, DARK_BLUE_BASE, 0, getHeight(), DARK_BLUE_DEEP);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }

    public JButton getBackButton() { return backButton; }
}
