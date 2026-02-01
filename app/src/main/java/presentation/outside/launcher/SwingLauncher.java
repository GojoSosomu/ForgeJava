package presentation.outside.launcher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import infrastructure.event.receiver.LoadingReceiver;
import presentation.enums.SuccessType;
import presentation.outside.channel.OutsideChannel;
import presentation.outside.swing.*;
import presentation.outside.swing.assembler.SwingChapterCardAssembler;
import presentation.service.BootService;
import presentation.service.assembler.ViewAssembler;

public class SwingLauncher extends Launcher {

    private final JFrame frame = new JFrame("ForgeJava");
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;

    @Override
    public void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit(frame, bootService);
            }
        });
        JPanel loadingPanel = new SwingLoadingPanel();
        switchPanel(loadingPanel, 700, 340);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        LoadingReceiver loadingReceiver = new LoadingReceiver(
            (OutsideChannel<LoadingView>) loadingPanel,
            viewAssembler
        );

        bootService.boot(loadingReceiver);

        SwingMainPanel maiPanel = new SwingMainPanel();
        
        SwingSignInPanel signInPanel = new SwingSignInPanel();

        SwingLogInPanel logInPanel = new SwingLogInPanel();

        logInPanel.getLogInButton().addActionListener(e -> {
            SuccessType result = logInSignInService.logIn(logInPanel.getUsername(), logInPanel.getPassword());
            if(result == SuccessType.LOG_IN_SUCCESS) {
                switchPanel(maiPanel);
            }
        });

        logInPanel.getSwitchToSignInButton().addActionListener(e ->  {
            logInPanel.clearFields();
            switchPanel(signInPanel, 380, 400);
        });

        signInPanel.getSignInButton().addActionListener(e -> {
            if(logInSignInService.signIn(
                signInPanel.getUsername(),
                signInPanel.getPassword(),
                signInPanel.getConfirmPassword()
            ) == SuccessType.SIGN_IN_SUCCESS) {
                signInPanel.clearFields();
                switchPanel(maiPanel);
            }
        });

        signInPanel.getSwitchToLoginButton().addActionListener(e -> {
            signInPanel.clearFields();
            switchPanel(logInPanel, 340, 340);
        });

        SwingChapterCoveragePanel chapterCoveragePanel = new SwingChapterCoveragePanel(
            new SwingChapterCardAssembler().assemble(
                chapterService.getAllChapters()
            ),
            chapterService
        );
        
        chapterCoveragePanel.getBackButton().addActionListener(e -> switchPanel(maiPanel));

        maiPanel.getStartButton().addActionListener(e ->
            switchPanel(chapterCoveragePanel, 1000, 600)
        );

        maiPanel.getQuitButton().addActionListener(e ->
            handleExit(frame, bootService)
        );
        
        switchPanel(logInPanel, 340, 340);
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

    private static void handleExit(JFrame frame, BootService bootService) {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Do you want to save before leaving?",
            "Exit Confirmation",
            JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Saving data...");
            frame.dispose();
            bootService.unboot();
            System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }
}
