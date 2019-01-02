import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppFileHandler extends AppDefault {

    @Override
    public void addUIControls(GridPane gridPane, Stage primaryStage) {
        Label headerLabel = new Label("Please select a .KDBX file");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Labels
        Label pathLabel = new Label("Path: ");
        Label fileButtonLabel = new Label("File selected:");
        Label selectedFileLabel = new Label("None");
        selectedFileLabel.setStyle("-fx-font-weight: bold");

        gridPane.add(pathLabel, 0, 1);
        gridPane.add(fileButtonLabel, 0, 2);
        gridPane.add(selectedFileLabel, 1, 2);

        // Open File Browser button & actions
        FileChooser fileChooser = new FileChooser();
        Button fileButton = new Button("Select File");
        gridPane.add(fileButton, 1, 1);

        Button openButton = new Button("Open File");
        gridPane.add(openButton, 0, 3, 2, 1);
        GridPane.setHalignment(openButton, HPos.CENTER);
        GridPane.setMargin(openButton, new Insets(20, 0, 20, 0));

        fileButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                for (Node child : gridPane.getChildren()) {
                    if (child instanceof Label && gridPane.getColumnIndex(child) == 1 && gridPane.getRowIndex(child) == 2) {
                        gridPane.getChildren().remove(child);
                        Label newSelectedFileLabel = new Label(file.getAbsolutePath());
                        newSelectedFileLabel.setStyle("-fx-font-weight: bold");
                        gridPane.add(newSelectedFileLabel, 1, 2);

                    } // TODO this seems to work fine, but throws 'ConcurrentModificationException'
                }
            }
        });

        openButton.setOnAction(event -> {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "TEST", "TEST Success");
        });
    }
}
