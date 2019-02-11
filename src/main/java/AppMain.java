import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    private static final String titleText = "SyncSafe";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(titleText);

        // Scene
        Scene fileChooserScene = AppFileChooser.getScene(primaryStage);
        primaryStage.setScene(fileChooserScene);
        primaryStage.show();
    }
}