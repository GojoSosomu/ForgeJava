package core.manager.domain;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import core.manager.domain.assembler.EntitySnapshotAssembler;
import core.manager.loader.LoadTarget;
import core.manager.saver.SaveTarget;
import core.model.dto.DTO;
import core.model.dto.progress.attainment.*;
import core.model.snapshot.progress.UserProgressSnapshot;
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
    }

    public void newUserProgress(String userName, String password, String salt) {
        UserProgressDTO userProgressDTO = new UserProgressDTO(
            userName,
            new UserAccount(
                password,
                salt
            ),
            new LessonProgress(
                Set.of()
            ),
            new ChapterProgress(
                Set.of()
            ),
            new ActivityProgress(
                Set.of()
            ),
            1
        );

        userProgressRepository.register(userName, userProgressDTO);
    }

    public void updateProgress(String userName, LessonProgress progress) {
        if (userProgressRepository.isExist(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void updateProgress(String userName, ChapterProgress progress) {
        if (userProgressRepository.isExist(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void updateProgress(String userName, ActivityProgress progress) {
        if (userProgressRepository.isExist(userName)) {
            UserProgressDTO existingProgress = userProgressRepository.get(userName);
            UserProgressDTO updatedProgress = existingProgress.updateProgress(progress);
            userProgressRepository.register(userName, updatedProgress);
        }
    }

    public void putDTO(String id, DTO dto) {
        userProgressRepository.register(id, (UserProgressDTO) dto);
    }

    @Override
    public UserProgressSnapshot from(String id) {
        UserProgressDTO dto = userProgressRepository.get(id);
        return new UserProgressSnapshot(
            Map.of(
                "userName", dto.id(),
                "userAccount", dto.userAccount(),
                "lessonProgress", dto.lessonProgress(),
                "chapterProgress", dto.chapterProgress(),
                "activityProgress", dto.activityProgress(),
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

    public Map<String, UserProgressDTO> getAll() {
        return userProgressRepository.getAll();
    }

    @Override
    public <T extends DTO> Map<String, T> getAllDTO() {
        return (Map<String, T>) userProgressRepository.getAll();
    }
}
