import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AppCredentialsInput {
    public static Scene getScene() {
        // Row 0 - Scene Header
        Label headerLabel = new Label("Please enter your credentials");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        HBox headerHb = new HBox(10);
        headerHb.setAlignment(Pos.CENTER);
        headerHb.getChildren().add(headerLabel);

        // Row 1 - Password input
        Label pwLabel = new Label("Password:");
        PasswordField pwField = new PasswordField();
        HBox pwHb = new HBox(10);
        pwHb.setAlignment(Pos.CENTER);
        pwHb.getChildren().addAll(pwLabel, pwField);

        // Row 2 - Open password file
        Button openBtn = new Button("Open");
//        openBtn.setOnAction();
        HBox openHb = new HBox(10);
        openHb.setAlignment(Pos.CENTER);
        openHb.getChildren().add(openBtn);

        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(headerHb, pwHb, openBtn);

        Scene scene = new Scene(vbox, 800, 400);
        return scene;
    }
}
