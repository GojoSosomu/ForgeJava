package presentation.outside.swing.template.activity;

import javax.swing.*;
import java.awt.*;
import core.model.view.progress.info.ScoreView;
import presentation.outside.channel.OutsideChannel;
import static presentation.outside.library.LibraryOfColor.*;

public class SwingActivityResultPanel extends JPanel implements OutsideChannel<ScoreView> {
    private final JLabel scoreLabel = new JLabel();
    private final JLabel statusLabel = new JLabel();

    public SwingActivityResultPanel(Runnable onDismiss) {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Center the content
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        scoreLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 72));
        scoreLabel.setForeground(ORANGE_BASED);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        statusLabel.setForeground(SUCCESS_GREEN);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton finishBtn = new JButton("RETURN TO CHAPTER");
        finishBtn.addActionListener(e -> onDismiss.run());
        finishBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalGlue());
        center.add(scoreLabel);
        center.add(statusLabel);
        center.add(Box.createRigidArea(new Dimension(0, 40)));
        center.add(finishBtn);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);
    }

    @Override
    public void render(ScoreView view) {
        // This is where the "Material" is displayed
        scoreLabel.setText(view.score() + " / " + view.total());
        statusLabel.setText(view.status());
        repaint();
    }
}