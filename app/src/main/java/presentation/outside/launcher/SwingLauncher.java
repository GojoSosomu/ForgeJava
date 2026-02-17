package presentation.outside.launcher;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import infrastructure.event.receiver.LoadingReceiver;
import presentation.enums.SuccessType;
import presentation.outside.channel.OutsideChannel;
import presentation.outside.swing.*;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.assembler.SwingChapterCardAssembler;
import presentation.service.BootService;
import presentation.service.assembler.ViewAssembler;

import static presentation.outside.library.LibraryOfProjectInfo.*;

public class SwingLauncher extends Launcher {

    private final JFrame frame = new JFrame(PROJECT_NAME);
    private JPanel loadingPanel;
    private SwingMainPanel maiPanel;
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

        try {
            Image icon = ImageIO.read(iconPathImporter.getIconPath());
            frame.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadingPanel = new SwingLoadingPanel();
        switchPanel(loadingPanel, 700, 340);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(false);

        LoadingReceiver loadingReceiver = new LoadingReceiver(
            (OutsideChannel<LoadingView>) loadingPanel,
            viewAssembler
        );

        bootService.boot(loadingReceiver);

        SwingAnimationRunner animationRunner = new SwingAnimationRunner(60);

        maiPanel = new SwingMainPanel();
        SwingSignInPanel signInPanel = new SwingSignInPanel(this);
        SwingLogInPanel logInPanel = new SwingLogInPanel(this);

        logInPanel.getLogInButton().addActionListener(e -> loginSuccessToMainPanel(logInPanel));
        logInPanel.getSwitchToSignInButton().addActionListener(e -> {
            logInPanel.reset();
            switchPanel(signInPanel, 400, 370);
            signInPanel.start();
        });

        signInPanel.getSignInButton().addActionListener(e -> signInSuccessToMainPanel(signInPanel));
        signInPanel.getSwitchToLoginButton().addActionListener(e -> {
            signInPanel.reset();
            switchPanel(logInPanel, 340, 340);
            logInPanel.start();
        });

        SwingChapterCoveragePanel chapterCoveragePanel = new SwingChapterCoveragePanel(
            new SwingChapterCardAssembler().assemble(chapterService.getAllChapters()),
            chapterService,
            animationRunner
        );
        
        chapterCoveragePanel.getBackButton().addActionListener(e -> switchPanel(maiPanel));
        maiPanel.getStartButton().addActionListener(e -> {
            switchPanel(chapterCoveragePanel, 1000, 600);
            chapterCoveragePanel.setUpLocked();
        });
        maiPanel.getQuitButton().addActionListener(e -> handleExit(frame, bootService));

        switchPanel(logInPanel, 340, 340);
    }

    public void switchPanel(JPanel panel, int width, int height) {
        SwingUtilities.invokeLater(() -> {
            panel.setPreferredSize(new Dimension(width, height));
            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
            
            panel.transferFocus(); 
        });
    }

    public void switchPanel(JPanel panel) {
        switchPanel(panel, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void loginSuccessToMainPanel(SwingLogInPanel logInPanel) {
        SuccessType result = logInSignInService.logIn(
            logInPanel.getUsername(), 
            logInPanel.getPassword()
        );
        switch (result) {
            case LOG_IN_SUCCESS -> {
                switchPanel(maiPanel);
            }
            case FAILURE_INCORRECT_PASSWORD -> {
                logInPanel.updateErrorLabelText("Incorrected Password");
            }
            case FAILURE_USER_NOT_FOUND -> {
                logInPanel.updateErrorLabelText("User don't exists");
            }
            case FAILURE_MISSING_FIELDS -> {
                logInPanel.updateErrorLabelText("Completed the format");
            }
            default -> {
                logInPanel.updateErrorLabelText("An unexpected error occurred. Please try again.");
            }
        }
    }

    public void signInSuccessToMainPanel(SwingSignInPanel signInPanel) {
        SuccessType result = logInSignInService.signIn(
            signInPanel.getUsername().trim(),
            signInPanel.getPassword().trim(),
            signInPanel.getConfirmPassword().trim()
        );
        switch(result) {
            case SIGN_IN_SUCCESS -> {
                switchPanel(maiPanel);
            }
            case FAILURE_MISSING_FIELDS -> {
                signInPanel.updateErrorLabelText("Please complete all required fields.");
            }
            case FAILURE_PASSWORD_MISMATCH -> {
                signInPanel.updateErrorLabelText("Passwords do not match. Please try again.");
            }
            case FAILURE_USER_EXISTS -> {
                signInPanel.updateErrorLabelText("An account with this email or username already exists.");
            }
            case FAILURE_PASSWORD_TOO_SHORT ->  {
                signInPanel.updateErrorLabelText("The Passwords are too short");
            }
            default -> {
                signInPanel.updateErrorLabelText("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void handleExit(JFrame frame, BootService bootService) {
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to save before leaving?", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
        if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.CLOSED_OPTION) return;
        if (confirm == JOptionPane.YES_OPTION) {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            frame.setEnabled(false);
            try { bootService.unboot(); } catch (Exception e) { 
                 JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        frame.dispose();
        System.exit(0);
    }
}