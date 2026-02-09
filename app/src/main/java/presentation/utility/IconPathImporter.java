package presentation.utility;

import java.net.URL;

public class IconPathImporter {

    public URL getIconPath() {
        return getClass().getResource("/images/icon/Icon.png");
    }
}
