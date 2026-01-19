package presentation.outside.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import core.model.view.loader.LoadingView;
import presentation.outside.channel.OutsideChannel;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class SwingLoadingPanel extends JPanel implements OutsideChannel<LoadingView> {

    private static final Color DEEP_BLUE = new Color(15, 25, 45);
    private static final Color FORGE_ORANGE = new Color(249, 115, 22);
    private static final Color GLOW_YELLOW = new Color(241, 196, 15);
    private static final Color TRACK_COLOR = new Color(30, 35, 50);
    private static final Color SUBTITLE_COLOR = new Color(150, 160, 180);

    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public SwingLoadingPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(60, 60, 60, 60));

        JLabel mainTitle = new JLabel("FORGE JAVA") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Vertical Gradient for the text
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
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 52));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainTitle.setPreferredSize(new Dimension(500, 70));
        mainTitle.setMaximumSize(new Dimension(500, 70));

        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int arc = 15;

                g2d.setColor(TRACK_COLOR);
                g2d.fill(new RoundRectangle2D.Double(0, 0, w, h, arc, arc));
                
                double percent = getPercentComplete();
                if (percent > 0) {
                    int progressWidth = (int) (w * percent);
                    GradientPaint gp = new GradientPaint(0, 0, FORGE_ORANGE, progressWidth, 0, GLOW_YELLOW);
                    g2d.setPaint(gp);
                    g2d.fill(new RoundRectangle2D.Double(0, 0, progressWidth, h, arc, arc));
                }
                
                g2d.dispose();
            }
        };
        progressBar.setPreferredSize(new Dimension(400, 14));
        progressBar.setMaximumSize(new Dimension(400, 14));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        statusLabel = new JLabel("INITIALIZING SYSTEM...");
        statusLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        statusLabel.setForeground(SUBTITLE_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(mainTitle);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(progressBar);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(statusLabel);
        add(Box.createVerticalGlue());
    }

    @Override
    public void render(LoadingView view) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(view.percentage());
            statusLabel.setText(view.currentTask());
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint background = new GradientPaint(
            0, 0, DEEP_BLUE, 
            0, getHeight(), Color.BLACK
        );
        g2d.setPaint(background);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(230, 126, 34, 20)); // Faint Orange Glow
        g2d.fillOval(getWidth()/2 - 150, getHeight()/2 - 150, 300, 300);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}