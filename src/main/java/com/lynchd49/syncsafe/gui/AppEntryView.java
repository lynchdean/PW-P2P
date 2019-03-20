package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.KdbxObject;
import com.lynchd49.syncsafe.utils.KdbxOps;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linguafranca.pwdb.Entry;

import java.io.IOException;

class AppEntryView {

    private static Stage window;
    private static Scene prevScene;

    private static TextField titleField;
    private static TextField usernameField;
    private static TextField passwordField;
    private static TextField urlField;
    private static TextField expiresField;
    private static TextArea notesArea;


    static Scene loadScene(Stage stage, Entry entry, KdbxObject kdbxObject) {
        window = stage;
        prevScene = window.getScene();

        int minLabelWidth = 130;
        int minFieldWidth = 570;

        // Entry Items
        Label titleLabel = new Label("Title:");
        titleLabel.setMinWidth(minLabelWidth);
        titleLabel.setAlignment(Pos.CENTER_RIGHT);
        titleField = new TextField(entry.getTitle());
        titleField.setMinWidth(minFieldWidth);
        HBox titleHbox = new HBox(10);
        titleHbox.getChildren().addAll(titleLabel, titleField);
        titleHbox.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setMinWidth(minLabelWidth);
        usernameLabel.setAlignment(Pos.CENTER_RIGHT);
        usernameField = new TextField(entry.getUsername());
        usernameField.setMinWidth(minFieldWidth);
        HBox usernameHbox = new HBox(10);
        usernameHbox.getChildren().addAll(usernameLabel, usernameField);
        usernameHbox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setAlignment(Pos.CENTER_RIGHT);
        passwordLabel.setMinWidth(minLabelWidth);
        passwordField = new TextField(entry.getPassword());
        passwordField.setMinWidth(minFieldWidth);
        HBox passwordHbox = new HBox(10);
        passwordHbox.getChildren().addAll(passwordLabel, passwordField);
        passwordHbox.setAlignment(Pos.CENTER_LEFT);

        Label urlLabel = new Label("URL:");
        urlLabel.setAlignment(Pos.CENTER_RIGHT);
        urlLabel.setMinWidth(minLabelWidth);
        urlField = new TextField(entry.getUrl());
        urlField.setMinWidth(minFieldWidth);
        HBox urlHbox = new HBox(10);
        urlHbox.getChildren().addAll(urlLabel, urlField);
        urlHbox.setAlignment(Pos.CENTER_LEFT);

        Label expiresLabel = new Label("Expires:");
        expiresLabel.setAlignment(Pos.CENTER_RIGHT);
        expiresLabel.setMinWidth(minLabelWidth);
        expiresField = new TextField(entry.getExpiryTime().toString());
        expiresField.setMinWidth(minFieldWidth);
        expiresField.setDisable(true);
        HBox expiresHbox = new HBox(10);
        expiresHbox.getChildren().addAll(expiresLabel, expiresField);
        expiresHbox.setAlignment(Pos.CENTER_LEFT);

        Label notesLabel = new Label("Notes:");
        notesLabel.setAlignment(Pos.CENTER_RIGHT);
        notesLabel.setMinWidth(minLabelWidth);
        notesArea = new TextArea(entry.getNotes());
        notesArea.setMinWidth(minFieldWidth);
        HBox notesHbox = new HBox(10);
        notesHbox.getChildren().addAll(notesLabel, notesArea);
        notesHbox.setAlignment(Pos.CENTER_LEFT);

        // Buttons and Organisation
        FontIcon saveIcon = new FontIcon("fa-check");
        saveIcon.setIconColor(Color.GREEN);
        Button saveBtn = new Button("Save", saveIcon);
        saveBtn.setOnAction(e -> {
            try {
                saveEntry(entry, kdbxObject);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        FontIcon cancelIcon = new FontIcon("fa-close");
        cancelIcon.setIconColor(Color.RED);
        Button cancelBtn = new Button("Cancel", cancelIcon);
        cancelBtn.setOnAction(e -> window.setScene(prevScene));

        HBox btnHbox = new HBox(10);
        btnHbox.getChildren().addAll(saveBtn, cancelBtn);
        btnHbox.setAlignment(Pos.CENTER_RIGHT);


        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(titleHbox, usernameHbox, passwordHbox, urlHbox, expiresHbox, notesHbox, btnHbox);
        vbox.setPadding(new Insets(10));

        return new Scene(vbox, 800, 400);
    }

    private static void saveEntry(Entry entry, KdbxObject kdbxObject) throws IOException {
        boolean altered = false;
        if (!entry.getTitle().equals(titleField.getText())) {
            entry.setTitle(titleField.getText());
            altered = true;
        }
        if (!entry.getUsername().equals(usernameField.getText())) {
            entry.setUsername(usernameField.getText());
            altered = true;
        }
        if (!entry.getPassword().equals(passwordField.getText())) {
            entry.setPassword(passwordField.getText());
            altered = true;
        }
        if (!entry.getUrl().equals(urlField.getText())) {
            entry.setUrl(urlField.getText());
            altered = true;
        }

        // TODO Handle Expiry changes

        if (!entry.getNotes().equals(notesArea.getText())) {
            entry.setNotes(notesArea.getText());
            altered = true;
        }

        if (altered) {
            KdbxOps.saveKdbx(kdbxObject);
        }
    }

}
