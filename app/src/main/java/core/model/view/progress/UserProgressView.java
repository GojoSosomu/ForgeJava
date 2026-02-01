package core.model.view.progress;

import core.model.view.View;
import core.model.view.progress.info.*;

public record UserProgressView(
    UserInfo userInfo,
    ProgressInfo progressInfo,
    int version
) implements View{

}
