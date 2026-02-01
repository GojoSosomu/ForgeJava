package core.manager.saver.order;

import java.nio.file.Paths;
import java.util.List;

import core.manager.loader.order.DefaultLoadingOrder;
import core.manager.saver.SavingRequest;

public class DefaultSavingOrder implements SavingOrder {
    @Override
    public List<SavingRequest> orders() {
        String userProgressPath;
        try {
            userProgressPath = Paths.get(
                DefaultLoadingOrder
                .class
                .getResource("/data/json/user_progress.json").toURI())
                .toString()
            ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve lesson.json resource path", e);
        }
        return List.of(
            new SavingRequest(
                "User Progress",
                userProgressPath,
                core.manager.saver.SaveType.USER_PROGRESS
            )
        );
    }
}
