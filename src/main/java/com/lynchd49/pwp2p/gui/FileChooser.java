package com.lynchd49.pwp2p.gui;

import com.lynchd49.pwp2p.gui.assets.Buttons;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

class FileChooser {

    private final static double minWidth = 70;
    private static Text actionStatus;

    private static File selectedFile;
    private static Text selectedFileText;

    static Scene loadScene(Stage window) {
        // File button
        FontIcon fileIcon = new FontIcon("fa-file");
        fileIcon.setIconColor(Color.CORNFLOWERBLUE);
        Button selectFileBtn = new Button("Choose .KDBX file", fileIcon);
        selectFileBtn.setOnAction(e -> showFileChooser(window));

        // File details
        Label selectedFileLabel = new Label("Selected File:");
        selectedFileText = new Text("None");
        HBox fileHbox = new HBox(10);
        fileHbox.setAlignment(Pos.CENTER_LEFT);
        fileHbox.getChildren().addAll(selectedFileLabel, selectedFileText);

        // Center VBox
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(selectFileBtn, fileHbox);

        // Right side button bar
        Region spacer = Buttons.getSpacerVGrow();
        Button openBtn = Buttons.getCheckBtn("Open", minWidth);
        openBtn.setOnAction(e -> openFile(window));
        Button quitBtn = Buttons.getCloseBtn("Quit", minWidth);
        quitBtn.setOnAction(e -> window.close());

        ToolBar buttonBar = new ToolBar();
        buttonBar.setMinWidth(minWidth + 10);
        buttonBar.setOrientation(Orientation.VERTICAL);
        buttonBar.getItems().addAll(spacer, openBtn, quitBtn);

        // Bottom status bar
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
        ToolBar statusBar = new ToolBar();
        statusBar.getItems().add(actionStatus);

        return new Scene(new BorderPane(vbox, null, buttonBar, statusBar, null));
    }

    private static void showFileChooser(Stage window) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        javafx.stage.FileChooser.ExtensionFilter fileExtensions = new javafx.stage.FileChooser.ExtensionFilter("KDBX (*.kdbx)", "*.kdbx");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File chosenFile = fileChooser.showOpenDialog(null);

        if (chosenFile != null) {
            selectedFile = chosenFile;
            actionStatus.setText(String.format("File selected: %s", selectedFile.getPath()));
            selectedFileText.setText(selectedFile.getName());
            openFile(window);
        } else {
            actionStatus.setText("File selection cancelled.");
        }
    }

    private static void openFile(Stage window) {
        if (selectedFile != null) {
            actionStatus.setFill(Color.FORESTGREEN);
            actionStatus.setText(String.format("Selected %s successfully.", selectedFile.getName()));
            Scene credentialsScene = CredentialsInput.loadScene(window, selectedFile);
            window.setScene(credentialsScene);
        } else {
            actionStatus.setFill(Color.FIREBRICK);
            actionStatus.setText("Failed to open: No file selected.");
            DialogAlert.display(window, "Error: No file selected", "Please select a file first!");
        }
    }
}