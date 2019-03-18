package com.lynchd49.syncsafe.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Entry;

class AppEntryView {

    private static Stage window;
    private static Scene prevScene;

    static Scene loadScene(Stage stage, Entry entry) {
        window = stage;
        prevScene = window.getScene();

        int minLabelWidth = 130;
        int minFieldWidth = 570;

        Label titleLabel = new Label("Title:");
        titleLabel.setMinWidth(minLabelWidth);
        titleLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField titleField = new TextField(entry.getTitle());
        titleField.setMinWidth(minFieldWidth);
        HBox titleHbox = new HBox(10);
        titleHbox.getChildren().addAll(titleLabel, titleField);
        titleHbox.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setMinWidth(minLabelWidth);
        usernameLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField usernameField = new TextField(entry.getUsername());
        usernameField.setMinWidth(minFieldWidth);
        HBox usernameHbox = new HBox(10);
        usernameHbox.getChildren().addAll(usernameLabel, usernameField);
        usernameHbox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setAlignment(Pos.CENTER_RIGHT);
        passwordLabel.setMinWidth(minLabelWidth);
        TextField passwordField = new TextField(entry.getPassword());
        passwordField.setMinWidth(minFieldWidth);
        HBox passwordHbox = new HBox(10);
        passwordHbox.getChildren().addAll(passwordLabel, passwordField);
        passwordHbox.setAlignment(Pos.CENTER_LEFT);

        Label urlLabel = new Label("URL:");
        urlLabel.setAlignment(Pos.CENTER_RIGHT);
        urlLabel.setMinWidth(minLabelWidth);
        TextField urlField = new TextField(entry.getUrl());
        urlField.setMinWidth(minFieldWidth);
        HBox urlHbox = new HBox(10);
        urlHbox.getChildren().addAll(urlLabel, urlField);
        urlHbox.setAlignment(Pos.CENTER_LEFT);

        Label expiresLabel = new Label("Expires:");
        expiresLabel.setAlignment(Pos.CENTER_RIGHT);
        expiresLabel.setMinWidth(minLabelWidth);
        TextField expiresField = new TextField(entry.getExpiryTime().toString());
        expiresField.setMinWidth(minFieldWidth);
        expiresField.setDisable(true);
        HBox expiresHbox = new HBox(10);
        expiresHbox.getChildren().addAll(expiresLabel, expiresField);
        expiresHbox.setAlignment(Pos.CENTER_LEFT);

        Label notesLabel = new Label("Notes:");
        notesLabel.setAlignment(Pos.CENTER_RIGHT);
        notesLabel.setMinWidth(minLabelWidth);
        TextArea notesArea = new TextArea(entry.getNotes());
        notesArea.setMinWidth(minFieldWidth);
        HBox notesHbox = new HBox(10);
        notesHbox.getChildren().addAll(notesLabel, notesArea);
        notesHbox.setAlignment(Pos.CENTER_LEFT);

        Button saveBtn = new Button("Save");
//        saveBtn.setOnAction();
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> returnToTableScene());
        HBox btnHbox = new HBox(10);
        btnHbox.getChildren().addAll(saveBtn, cancelBtn);
        btnHbox.setAlignment(Pos.CENTER_RIGHT);


        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(titleHbox, usernameHbox, passwordHbox, urlHbox, expiresHbox, notesHbox, btnHbox);
        vbox.setPadding(new Insets(10));

        return new Scene(vbox, 800, 400);
    }

    static void returnToTableScene() {
        window.setScene(prevScene);
    }

}
