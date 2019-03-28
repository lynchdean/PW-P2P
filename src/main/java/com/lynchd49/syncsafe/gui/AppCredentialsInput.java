package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.gui.assets.Buttons;
import com.lynchd49.syncsafe.utils.KdbxObject;
import com.lynchd49.syncsafe.utils.KdbxOps;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;

import java.io.File;
import java.io.IOException;

class AppCredentialsInput {

    private final static double minWidth = 80;
    private static Text actionStatus;

    static Scene loadScene(Stage window, File file) {
        Scene prevScene = window.getScene();

        Label headerLabel = new Label("Please enter your credentials");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Password input
        Label pwLabel = new Label("Password:");
        PasswordField pwField = new PasswordField();
        HBox.setHgrow(pwField, Priority.ALWAYS);
        pwField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) checkCredentials(pwField.getText(), file, window);
        });
        HBox pwHbox = new HBox(10);
        pwHbox.setAlignment(Pos.CENTER_LEFT);
        pwHbox.getChildren().addAll(pwLabel, pwField);

        // Centre VBox
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(headerLabel, pwHbox);

        // Right side button bar
        Region spacer = Buttons.getSpacerVGrow();
        Button cancelBtn = Buttons.getCloseBtn("Cancel", minWidth);
        cancelBtn.setOnAction(e -> returnToPrevScene(window, prevScene));
        Button openBtn = Buttons.getCheckBtn("Open", minWidth);
        openBtn.setOnAction(e -> checkCredentials(pwField.getText(), file, window));

        ToolBar buttonBar = new ToolBar();
        buttonBar.setMinWidth(minWidth + 10);
        buttonBar.setOrientation(Orientation.VERTICAL);
        buttonBar.getItems().addAll(spacer, openBtn, cancelBtn);

        // Bottom status bar
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
        ToolBar statusBar = new ToolBar();
        statusBar.getItems().add(actionStatus);

        return new Scene(new BorderPane(vbox, null, buttonBar, statusBar, null));
    }

    private static void checkCredentials(String pwInput, File file, Stage window) {
        try {
            Database db = KdbxOps.loadKdbx(file, pwInput);
            KdbxObject kdbxObject = new KdbxObject(db, file.getPath(), pwInput);
            actionStatus.setText("Correct credentials!");
            actionStatus.setFill(Color.GREEN);
            Scene homeScene = AppHome.loadScene(window, kdbxObject);
            window.setScene(homeScene);
        } catch (IOException e) {
            actionStatus.setText("Error accessing file");
            actionStatus.setFill(Color.FIREBRICK);
        } catch (IllegalStateException e) {
            actionStatus.setText("Incorrect credentials");
            actionStatus.setFill(Color.FIREBRICK);
        }
    }

    private static void returnToPrevScene(Stage window, Scene prevScene) {
        window.setScene(prevScene);
    }
}
