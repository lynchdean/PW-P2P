import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class AppCredentialsInput {

    private static Text actionStatus;

    // Window & Scenes
    private static Stage window;
    private static Scene prevScene;

    static Scene getScene(Stage stage) {
        window = stage;
        prevScene = window.getScene();

        // Row 0 - Scene Header
        Label headerLabel = new Label("Please enter your credentials");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        HBox headerHb = new HBox(10);
        headerHb.setAlignment(Pos.CENTER);
        headerHb.getChildren().add(headerLabel);

        // Row 1 - Password input
        Label pwLabel = new Label("Password:");
        PasswordField pwField = new PasswordField();
        pwField.setPrefWidth(200);
        HBox pwHb = new HBox(10);
        pwHb.setAlignment(Pos.CENTER);
        pwHb.getChildren().addAll(pwLabel, pwField);

        // Row 2 - Open password file & Button to return to previous window
        Button returnBtn = new Button("Cancel");
        returnBtn.setOnAction(e -> returnToPrevScene());
        Button openBtn = new Button("Open");
        openBtn.setOnAction(e -> checkCredentials(pwField.getText()));

        HBox openBtnHb = new HBox(10);
        openBtnHb.setAlignment(Pos.BOTTOM_RIGHT);
        openBtnHb.getChildren().addAll(returnBtn, openBtn);

        // Row 3 - Status message
        actionStatus = new Text("");
        actionStatus.setFill(Color.FIREBRICK);

        // VBox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(headerHb, pwHb, openBtnHb, actionStatus);

        return new Scene(vbox, 800, 400);
    }

    private static void checkCredentials(String pwInput) {
        if (pwInput.equals("123")) {
            actionStatus.setText("Correct credentials!");
            actionStatus.setFill(Color.GREEN);
        } else {
            actionStatus.setText("Incorrect credentials!");
            actionStatus.setFill(Color.FIREBRICK);
        }
    }

    private static void returnToPrevScene() {
        window.setScene(prevScene);
    }
}
