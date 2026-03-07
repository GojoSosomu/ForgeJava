package presentation.outside.swing;

import javax.swing.*;
import java.awt.*;

public class SwingTransitionPanel extends JPanel {

    private JPanel currentPanel;

    public SwingTransitionPanel() {
        setLayout(new BorderLayout());
    }

    public void setPanel(JPanel panel) {

        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = panel;

        add(panel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
