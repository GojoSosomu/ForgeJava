package presentation.outside.fx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScene extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/javafx/sceneFXML/editor.fxml"));
        Scene scene = new Scene(root, 1366, 768);
        scene.getStylesheets().add(getClass().getResource("/javafx/sceneCSS/editor.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ForgeJava FXML Demo");
        stage.show();
    }
}
