import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class OpenDbForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Open Database");

        GridPane gridPane = createOpenDbFormPane();
        addUIControls(gridPane);
        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private GridPane createOpenDbFormPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnConstraints.setHalignment(HPos.RIGHT);

        ColumnConstraints columnTwoConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstraints.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnConstraints, columnTwoConstraints);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        Label headerLabel = new Label("Welcome");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        //  Path Label
        Label pathLabel = new Label("Path: ");
        gridPane.add(pathLabel, 0, 1);

        // Path Text Field
        TextField pathField = new TextField();
        gridPane.add(pathField, 1, 1);

        // Password Label
        Label passwordLabel = new Label("Password: ");
        gridPane.add(passwordLabel, 0, 2);

        // Password PasswordField
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        Button openButton = new Button("Open");
        gridPane.add(openButton, 0, 3, 2, 1);
        GridPane.setHalignment(openButton, HPos.RIGHT);
        GridPane.setMargin(openButton, new Insets(20, 0, 20, 0));

        openButton.setOnAction(event -> {
            if (pathField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter the path");
                return;
            }

            if (passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                        "Form Error!", "Please enter a password");
                return;
            }

            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),
                    "TEST", "TEST Success");
        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
