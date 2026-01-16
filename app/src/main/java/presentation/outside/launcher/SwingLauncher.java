package presentation.outside.launcher;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import infrastructure.event.receiver.LoadingReceiver;
import presentation.outside.channel.OutsideChannel;
import presentation.outside.swing.*;
import presentation.outside.swing.assembler.SwingChapterCardAssembler;
import presentation.service.assembler.ViewAssembler;

public class SwingLauncher extends Launcher {

    private final JFrame frame = new JFrame("The Ahas LarongGame");
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;

    @Override
    public void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loadingPanel = new SwingLoadingPanel();
        frame.setContentPane(loadingPanel);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        LoadingReceiver loadingReceiver = new LoadingReceiver(
            (OutsideChannel<LoadingView>) loadingPanel,
            viewAssembler
        );

        bootService.boot(loadingReceiver);

        SwingMainPanel maiPanel = new SwingMainPanel();

        SwingChapterCoveragePanel chapterCoveragePanel = new SwingChapterCoveragePanel(
            new SwingChapterCardAssembler().assemble(
                chapterService.getAllChapters()
            ),
            chapterService
        );
        
        chapterCoveragePanel.getBackButton().addActionListener(e -> switchPanel(maiPanel));

        maiPanel.getStartButton().addActionListener(e ->
            switchPanel(chapterCoveragePanel)
        );

        maiPanel.getQuitButton().addActionListener(e ->
            System.exit(0)
        );
        
        switchPanel(maiPanel);
    }

    public void switchPanel(JPanel panel, int width, int height) {
        SwingUtilities.invokeLater(() -> {
            panel.setPreferredSize(new Dimension(width, height));
            panel.setFocusable(true);
            
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            
            frame.revalidate();
            frame.repaint();
            
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }

            panel.requestFocusInWindow();
            frame.toFront();
            frame.setAlwaysOnTop(false); 
        });
    }

    public void switchPanel(JPanel panel) {
        switchPanel(panel, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}