package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.KdbxObject;
import com.lynchd49.syncsafe.utils.KdbxOps;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;

import java.io.File;
import java.io.IOException;

class AppCredentialsInput {

    private static Text actionStatus;

    static Scene loadScene(Stage window, File file) {
        Scene prevScene = window.getScene();

        Label headerLabel = new Label("Please enter your credentials");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Row 1 - Password input
        Label pwLabel = new Label("Password:");
        PasswordField pwField = new PasswordField();
        pwField.setPrefWidth(200);
        pwField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkCredentials(pwField.getText(), file, window);
            }
        });
        HBox pwHb = new HBox(10);
        pwHb.setAlignment(Pos.CENTER);
        pwHb.getChildren().addAll(pwLabel, pwField);

        // Row 2 - Open password file & Button to return to previous window
        Button returnBtn = new Button("Cancel");
        returnBtn.setOnAction(e -> returnToPrevScene(window, prevScene));
        Button openBtn = new Button("Open");
        openBtn.setOnAction(e -> checkCredentials(pwField.getText(), file, window));

        HBox openBtnHb = new HBox(10);
        openBtnHb.setAlignment(Pos.BOTTOM_RIGHT);
        openBtnHb.getChildren().addAll(returnBtn, openBtn);

        // Row 3 - Status message
        actionStatus = new Text("");
        actionStatus.setFill(Color.FIREBRICK);

        // VBox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(headerLabel, pwHb, openBtnHb, actionStatus);

        return new Scene(vbox, 800, 400);
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
