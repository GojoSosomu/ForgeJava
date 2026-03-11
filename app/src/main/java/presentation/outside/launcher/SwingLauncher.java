package presentation.outside.launcher;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.activity.ActivityView;
import core.model.view.loader.LoadingView;
import core.model.view.progress.UserProgressView;
import infrastructure.event.receiver.LoadingReceiver;
import presentation.enums.SuccessType;
import presentation.outside.channel.OutsideChannel;
import presentation.outside.swing.*;
import presentation.outside.swing.animation.SwingAnimationRunner;
import presentation.outside.swing.assembler.*;
import presentation.service.BootService;
import presentation.service.assembler.ViewAssembler;

import static presentation.outside.library.LibraryOfProjectInfo.*;

public class SwingLauncher extends Launcher {

    private final JFrame frame = new JFrame(PROJECT_NAME);
    private JPanel loadingPanel;
    private SwingMainPanel mainPanel;
    private SwingChapter chapterPanel;
    private final SwingTransitionPanel transition = new SwingTransitionPanel();
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 700;

    @Override
    public void start(ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler) {
        // --- STEP 1: Frame setup (same as before, runs on EDT) ---
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
        frame.setContentPane(transition);

        LoadingReceiver loadingReceiver = new LoadingReceiver(
            (OutsideChannel<LoadingView>) loadingPanel,
            viewAssembler
        );

        // --- STEP 2: Run boot on a background thread ---
        SwingWorker<Void, Void> bootWorker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {
                // Runs OFF the EDT — loading is safe here
                bootService.boot(loadingReceiver);
                return null;
            }

            @Override
            protected void done() {
                // Runs BACK ON the EDT — safe to build and touch UI here

                // Check if boot threw an exception
                try {
                    get(); // rethrows any exception from doInBackground
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Failed to load application data:\n" + e.getMessage(),
                        "Boot Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    System.exit(1);
                    return;
                }

                // --- STEP 3: Build all panels (same as before, now inside done()) ---
                SwingAnimationRunner animationRunner = new SwingAnimationRunner(60);

                mainPanel = new SwingMainPanel();
                SwingSignInPanel signInPanel = new SwingSignInPanel(SwingLauncher.this);
                SwingLogInPanel logInPanel = new SwingLogInPanel(SwingLauncher.this);

                logInPanel.getLogInButton().addActionListener(e -> loginSuccessToMainPanel(logInPanel));
                logInPanel.getSwitchToSignInButton().addActionListener(e -> {
                    logInPanel.reset();
                    switchPanel(signInPanel, 400, 370);
                    signInPanel.start();
                });

                signInPanel.getSignInButton().addActionListener(e -> signInSuccessToMainPanel(signInPanel));
                signInPanel.getSwitchToLoginButton().addActionListener(e -> {
                    signInPanel.reset();
                    switchPanel(logInPanel, 380, 340);
                    logInPanel.start();
                });

                SwingCreditPanel creditPanel = new SwingCreditPanel();
                creditPanel.getBackButton().addActionListener(e -> switchPanel(mainPanel));

                SwingChapterCoveragePanel chapterCoveragePanel = new SwingChapterCoveragePanel(
                    new SwingChapterCardAssembler().assemble(chapterService.getAllChapters()),
                    new SwingChapterIntroAssembler().assembleIntroTemplates(chapterService.getAllChapters()),
                    chapterService,
                    animationRunner,
                    SwingLauncher.this
                );
                chapterCoveragePanel.getBackButton().addActionListener(e -> switchPanel(mainPanel));

                mainPanel.getStartButton().addActionListener(e -> {
                    chapterCoveragePanel.updateAvailability();
                    switchPanel(chapterCoveragePanel, 1000, 600);
                });
                mainPanel.getProgressionButton().addActionListener(e -> {

                    UserProgressView progressView = userService.getCurrentProgressView(); 

                    SwingProgressionPanel progressionPanel = new SwingProgressionPanel(
                        progressView.progressInfo(), 
                        () -> switchPanel(mainPanel)
                    );

                    // 3. Switch the CEO's view
                    switchPanel(progressionPanel);
                });

                mainPanel.getQuitButton().addActionListener(e -> handleExit(frame, bootService));
                mainPanel.getLogoutButton().addActionListener(e -> {
                    logInSignInService.resetCurrentUser();
                    try { bootService.save(); } catch (Exception exception) { 
                        JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    logInPanel.reset();
                    switchPanel(logInPanel, 400, 370);
                    logInPanel.start();
                });
                mainPanel.getCreditButton().addActionListener(e -> switchPanel(creditPanel));

                // --- STEP 4: Navigate to first screen (same as before) ---
                SuccessType result = logInSignInService.logInCurrentUser();
                if (result == SuccessType.LOG_IN_SUCCESS) {
                    switchPanel(mainPanel);
                    mainPanel.getNameLabel().setText("Welcome, " + logInSignInService.getCurrentUserName());
                } else {
                    switchPanel(logInPanel, 380, 340);
                }
            }
        };

        bootWorker.execute(); // non-blocking — returns immediately, EDT stays free
    }

    public void switchPanel(JPanel panel, int width, int height) {
        // 1. Hide the frame immediately
        frame.setVisible(false);

        SwingUtilities.invokeLater(() -> {
            // 2. We are now "behind the curtain." Do the heavy work.
            frame.setSize(width, height);
            transition.setPanel(panel);
            frame.setLocationRelativeTo(null); 
            
            // 3. Request the UI to update its internal layout 
            frame.revalidate();
            frame.repaint();

            // 4. Only once EVERYTHING above is finished, schedule the "Show"
            SwingUtilities.invokeLater(() -> {
                frame.setVisible(true);
            });
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
                try { bootService.save(); } catch (Exception exception) { 
                        JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mainPanel.getNameLabel().setText("Welcome, " + logInSignInService.getCurrentUserName());
                switchPanel(mainPanel);
            }
            case FAILURE_INCORRECT_PASSWORD -> {
                logInPanel.errorShowMessage("Incorrect password. Please try again.");
            }
            case FAILURE_USER_NOT_FOUND -> {
                logInPanel.errorShowMessage("User not found. Please check your credentials.");
            }
            case FAILURE_MISSING_FIELDS -> {
                logInPanel.errorShowMessage("Please complete all required fields.");
            }
            default -> {
                logInPanel.errorShowMessage("An unexpected error occurred. Please try again.");
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
                switchPanel(mainPanel);
                mainPanel.getNameLabel().setText("Welcome, " + logInSignInService.getCurrentUserName());
            }
            case FAILURE_MISSING_FIELDS -> {
                signInPanel.errorShowMessage("Please complete all required fields.");
            }
            case FAILURE_PASSWORD_MISMATCH -> {
                signInPanel.errorShowMessage("Passwords do not match. Please try again.");
            }
            case FAILURE_USER_EXISTS -> {
                signInPanel.errorShowMessage("An account with this email or username already exists.");
            }
            case FAILURE_PASSWORD_TOO_SHORT ->  {
                signInPanel.errorShowMessage("The passwords are too short. Please enter at least 8 characters.");
            }
            case FAILURE_PASSWORD_MISSING_SYMBOL -> {
                signInPanel.errorShowMessage("Must contain one uppercase letter, number, and special character.");
            }
            default -> {
                signInPanel.errorShowMessage("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void handleExit(JFrame frame, BootService bootService) {
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to save before leaving?", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
        if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.CLOSED_OPTION) return;
        if (confirm == JOptionPane.YES_OPTION) {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            frame.setEnabled(false);
            try { bootService.save(); } catch (Exception e) { 
                 JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        frame.dispose();
        bootService.unboot();
    }

    public void startChapter(String currentItem) {
        chapterPanel = new SwingChapter(
            chapterService,
            chapterService.getChapter(currentItem), 
            new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT),
            this
        );
        chapterPanel.showIntro();
    }

    public void startLesson(String currentItem) {
        SwingLessonPanel lessonPanel = new SwingLessonPanel(
            lessonService.getLesson(currentItem), 
            this, 
            () -> this.completedLessonItem(currentItem)
        );
        lessonPanel.start();

        switchPanel(lessonPanel);
    }

    public void endChapter(String id) {
        userService.completedChapter(id);
        returnChapterMenu();
    }

    public void returnChapterMenu() {
        switchPanel(mainPanel);
    }

    public void returnChapterSequence() {
        chapterPanel.showSequence();
    }

    private void completedLessonItem(String id) {
        userService.completedLessonItem(id);
        chapterPanel.updatedSequencePanel(
            chapterService,
            chapterService.getChapter(chapterPanel.id())
        );
        returnChapterSequence();
    }

    public void startActivity(String id) {
        ActivityView view = activityService.getActivity(id);

        SwingActivity activityCoordinator = new SwingActivity(
            userService,
            activityService,
            view, 
            this, 
            () -> {
                chapterPanel.updatedSequencePanel(chapterService, chapterService.getChapter(chapterPanel.id()));
                returnChapterSequence();
            }
        );
        
        activityCoordinator.show();
    }
}
