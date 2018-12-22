import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static javafx.geometry.HPos.CENTER;

public class AppCredentialsInput extends AppDefault {
    @Override
    public void addUIControls(GridPane gridPane, Stage primaryStage) {
        Label headerLabel = new Label("Welcome");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Password Label
        Label passwordLabel = new Label("Password: ");
        gridPane.add(passwordLabel, 0, 2);

        // Password PasswordField
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        // Open Database Button
        Button openButton = new Button("Open");
        gridPane.add(openButton, 0, 3, 2, 1);
        GridPane.setHalignment(openButton, CENTER);
        GridPane.setMargin(openButton, new Insets(20, 0, 20, 0));

        openButton.setOnAction(event -> {
            if (passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter a password");
                return;
            }

            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                    "TEST", "Path: " + "\nPassword: " + passwordField.getText());
        });
    }
}
