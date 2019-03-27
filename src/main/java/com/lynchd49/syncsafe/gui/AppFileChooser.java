package com.lynchd49.syncsafe.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

class AppFileChooser {

    private static Text actionStatus;
    private final static double minWidth = 70;

    // Window & Scenes
    private static Stage window;

    // File
    private static File selectedFile;
    private static Text selectedFileText;

    static Scene loadScene(Stage stage) {
        window = stage;

        Label headerLabel = new Label("Please select a .KDBX file:");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // File button
        FontIcon fileIcon = new FontIcon("fa-file");
        fileIcon.setIconColor(Color.CORNFLOWERBLUE);
        Button selectFileBtn = new Button("Choose file...", fileIcon);
        selectFileBtn.setOnAction(e -> showFileChooser());

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
        vbox.getChildren().addAll(headerLabel, selectFileBtn, fileHbox);

        // Right side button bar
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);

        FontIcon openIcon = new FontIcon("fa-check");
        openIcon.setIconColor(Color.GREEN);
        Button openBtn = new Button("Open", openIcon);
        openBtn.setMinWidth(minWidth);
        openBtn.setOnAction(e -> openFile());

        FontIcon quitIcon = new FontIcon("fa-close");
        quitIcon.setIconColor(Color.RED);
        Button quitBtn = new Button("Quit", quitIcon);
        quitBtn.setMinWidth(minWidth);
        quitBtn.setOnAction(e -> window.close());

        ToolBar buttonBar = new ToolBar();
        buttonBar.setMinWidth(minWidth + 10);
        buttonBar.setOrientation(Orientation.VERTICAL);
        buttonBar.getItems().addAll(spacer, quitBtn, openBtn);

        // Bottom status bar
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
        ToolBar statusBar = new ToolBar();
        statusBar.getItems().add(actionStatus);

        // Main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setRight(buttonBar);
        borderPane.setBottom(statusBar);

        return new Scene(borderPane);
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
            openFile();
        } else {
            actionStatus.setText("File selection cancelled.");
        }
    }

    private static void openFile() {
        if (selectedFile != null) {
            actionStatus.setFill(Color.FORESTGREEN);
            actionStatus.setText(String.format("Selected %s successfully.", selectedFile.getName()));
            Scene credentialsScene = AppCredentialsInput.loadScene(window, selectedFile);
            window.setScene(credentialsScene);
        } else {
            actionStatus.setFill(Color.FIREBRICK);
            actionStatus.setText("Failed to open: No file selected.");
            DialogAlert.display(window, "Error: No file selected", "Please select a file first!");
        }
    }
}