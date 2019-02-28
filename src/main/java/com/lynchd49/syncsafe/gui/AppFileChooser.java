package com.lynchd49.syncsafe.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

class AppFileChooser {

    private static Text actionStatus;

    // Window & Scenes
    private static Stage window;

    // File
    private static File selectedFile;
    private static Text selectedFileText;

    static Scene loadScene(Stage stage) {
        window = stage;

        // Row 0 - Scene header
        Label headerLabel = new Label("Please select .KDBX file");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        HBox headerHb = new HBox();
        headerHb.setAlignment(Pos.CENTER);
        headerHb.getChildren().add(headerLabel);

        // Row 1 - Choose file actions
        Button selectFileBtn = new Button("Choose a file...");
        selectFileBtn.setOnAction(e -> showFileChooser());
        HBox selectFileActionHb = new HBox(10);
        selectFileActionHb.setAlignment(Pos.CENTER);
        selectFileActionHb.getChildren().add(selectFileBtn);

        // Row 2 - Selected file
        Label selectedFileLabel = new Label("Selected File:");
        HBox selectedFileHb = new HBox(10);
        selectedFileHb.setAlignment(Pos.CENTER);
        selectedFileText = new Text("None");
        selectedFileHb.getChildren().addAll(selectedFileLabel, selectedFileText);

        // Row 3 - Open File
        Button openFileBtn = new Button("Open");
        openFileBtn.setOnAction(e -> openFile());
        HBox buttonHb2 = new HBox(10);
        buttonHb2.setAlignment(Pos.BOTTOM_RIGHT);
        buttonHb2.getChildren().addAll(openFileBtn);

        // Row 4 - Status message
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

        // VBox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(headerHb, selectFileActionHb, selectedFileHb, buttonHb2, actionStatus);

        return new Scene(vbox, 800, 400);
    }

    private static void showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("KDBX (*.kdbx)", "*.kdbx");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File chosenFile = fileChooser.showOpenDialog(null);

        if (chosenFile != null) {
            selectedFile = chosenFile;
            actionStatus.setText(String.format("File selected: %s", selectedFile.getPath()));
            selectedFileText.setText(selectedFile.getName());
        } else {
            actionStatus.setText("File selection cancelled.");
        }
    }

    private static void openFile() {
        if (selectedFile != null) {
            actionStatus.setText(String.format("Opened %s successfully.", selectedFile.getName()));
            Scene credentialsScene = AppCredentialsInput.loadScene(window, selectedFile);
            window.setScene(credentialsScene);
        } else {
            actionStatus.setText("Failed to open: No file selected.");
            AlertBox.display(window, "Error: No file selected", "Please select a file first!");
        }
    }
}