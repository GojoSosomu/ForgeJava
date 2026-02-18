package core.manager.domain;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.manager.saver.SaveTarget;
import core.model.dto.DTO;
import core.model.dto.progress.attainment.*;
import core.model.snapshot.progress.UserProgressSnapshot;
import core.model.dto.progress.UserDatabaseDTO;
import core.model.dto.progress.UserProgressDTO;
import core.repository.UserProgressRepository;

public class UserProgressManager implements LoadTarget, SaveTarget, EntitySnapshotAssembler<UserProgressSnapshot> {
    private UserProgressRepository userProgressRepository;

    public UserProgressManager(
        UserProgressRepository userProgressRepository
    ) {
        this.userProgressRepository = userProgressRepository;
    }

    public void printAllUserProgress() {
        userProgressRepository.getAll().forEach((id, progress) -> {
            System.out.println("User Progress ID: " + id);
            System.out.println("User Progress Data: " + progress +"\n");
        });

        System.out.println("Current User: " + userProgressRepository.getCurrentUsername() + "\n");
    }

    public void newUserProgress(String userName, String password, String salt) {
        UserProgressDTO userProgressDTO = new UserProgressDTO(
            userName,
            new UserAccount(
                password,
                salt
            ),
            new LessonProgress(
                List.of()
            ),
            new ChapterProgress(
                List.of(),
                (byte)1
            ),
            new ActivityProgress(
                List.of()
            ),
            1
        );

        userProgressRepository.register(userName, userProgressDTO);
        userProgressRepository.setCurrentUser(userName);
    }

    public byte getCurrentChapter(String userName) {
        if(userExists(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            return existingProgress.chapterProgress().currentChapter();
        }
        return 1;
    }

    public void updateProgress(String userName, LessonProgress progress) {
        if (userExists(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void updateProgress(String userName, ChapterProgress progress) {
        if (userExists(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void updateProgress(String userName, ActivityProgress progress) {
        if (userExists(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void putDTO(String id, DTO dto) {
        UserDatabaseDTO userDatabaseDTO = (UserDatabaseDTO) dto;
        userProgressRepository.setCurrentUser(id);
        for(UserProgressDTO user : userDatabaseDTO.users())
            userProgressRepository.register(user.id(), user);
    }

    @Override
    public UserProgressSnapshot from(String id) {
        UserProgressDTO dto = userProgressRepository.get(id);
        return new UserProgressSnapshot(
            Map.of(
                "userName", dto.id(),
                "userAccount", Map.of(
                    "password", dto.userAccount().password(),
                    "salt", dto.userAccount().salt()
                ),
                "lessonProgress", Map.of(
                    "completedLessons", dto.lessonProgress().completedLessons()
                ),
                "chapterProgress", Map.of(
                    "completedChapters", dto.chapterProgress().completedChapters(),
                    "currentChapter", dto.chapterProgress().currentChapter()
                ),
                "activityProgress", Map.of(
                    "completedActivities", dto.activityProgress().completedActivities()
                ),
                "version", dto.version()
        ));
    }

    public String generateSecureSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashString(String input, String salt) {
        String hashedValue = null;
        do {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] bytes = md.digest((salt + input + salt).getBytes());

                StringBuilder ian = new StringBuilder();
                for (byte b : bytes) {
                    ian.append(String.format("%02x", b));
                }

                hashedValue = ian.toString();

            } catch (Exception e) {
                hashedValue = null;
            }
        } while(hashedValue == null);
        return hashedValue;
    }

    public boolean authenticate(String userName, String hashPassword) {
        UserAccount userAccount = userProgressRepository.get(userName).userAccount();
        if(hashPassword.equals(userAccount.password())) {
            System.out.println("Authentication successful for user: " + userName);
            userProgressRepository.setCurrentUser(userName);
        } else {
            System.out.println("Authentication failed for user: " + userName);
        }
        return hashPassword.equals(userAccount.password());
    }

    public boolean userExists(String userName) {
        return userProgressRepository.isExist(userName);
    }

    public String getSaltByUserName(String userName) {
        UserAccount userAccount = userProgressRepository.get(userName).userAccount();
        return userAccount.salt();
    }

    @Override
    public <T extends DTO> Map<String, T> getAllDTO() { 
        System.out.println(Map.of("USER_DATABASE", userProgressRepository.getUserDatabase()));       
        return (Map<String, T>) Map.of("USER_DATABASE", userProgressRepository.getUserDatabase());
    }

    public UserProgressDTO getCurrentUser() {
        return userProgressRepository.getCurrentUser();
    }

    public void setCurrentUser(String userName) {
        userProgressRepository.setCurrentUser(userName);
    }
}
