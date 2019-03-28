package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.gui.assets.Buttons;
import com.lynchd49.syncsafe.utils.EntryView;
import com.lynchd49.syncsafe.utils.KdbxObject;
import com.lynchd49.syncsafe.utils.KdbxOps;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;

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

    private final static double minWidth = 80;


    static Scene loadScene(Stage stage,  ObservableList<EntryView> tableData, Entry entry, KdbxObject kdbxObject) {
        window = stage;
        prevScene = window.getScene();

        // Entry Items
        Label titleLabel = new Label("Title:");
        entryLabelHelper(titleLabel);
        titleField = new TextField(entry.getTitle());
        HBox.setHgrow(titleField, Priority.ALWAYS);
        HBox titleHbox = entryHboxHelper(titleLabel, titleField);

        Label usernameLabel = new Label("Username:");
        entryLabelHelper(usernameLabel);
        usernameField = new TextField(entry.getUsername());
        HBox.setHgrow(usernameField, Priority.ALWAYS);
        HBox usernameHbox = entryHboxHelper(usernameLabel, usernameField);

        Label passwordLabel = new Label("Password:");
        entryLabelHelper(passwordLabel);
        passwordField = new TextField(entry.getPassword());
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        HBox passwordHbox = entryHboxHelper(passwordLabel, passwordField);

        Label urlLabel = new Label("URL:");
        entryLabelHelper(urlLabel);
        urlField = new TextField(entry.getUrl());
        HBox.setHgrow(urlField, Priority.ALWAYS);
        HBox urlHbox = entryHboxHelper(urlLabel, urlField);

        Label expiresLabel = new Label("Expires:");
        entryLabelHelper(expiresLabel);
        expiresField = new TextField(entry.getExpiryTime().toString());
        HBox.setHgrow(expiresField, Priority.ALWAYS);
        expiresField.setDisable(true);
        HBox expiresHbox = entryHboxHelper(expiresLabel, expiresField);

        Label notesLabel = new Label("Notes:");
        entryLabelHelper(notesLabel);
        notesArea = new TextArea(entry.getNotes());
        HBox.setHgrow(notesArea, Priority.ALWAYS);
        HBox notesHbox = entryHboxHelper(notesLabel, notesArea);

        // Toolbar and layout
        VBox fieldsVbox = new VBox(10);
        fieldsVbox.getChildren().addAll(titleHbox, usernameHbox, passwordHbox, urlHbox, expiresHbox, notesHbox);
        fieldsVbox.setPadding(new Insets(10));
        HBox.setHgrow(fieldsVbox, Priority.ALWAYS);

        ToolBar toolBar = getToolbar(tableData, entry, kdbxObject);
        HBox mainHbox = new HBox(10);
        mainHbox.getChildren().addAll(fieldsVbox, toolBar);

        return new Scene(mainHbox, 800, 400);
    }

    private static void entryLabelHelper(Label titleLabel) {
        titleLabel.setMinWidth(minWidth);
        titleLabel.setAlignment(Pos.CENTER_RIGHT);
    }

    @NotNull
    private static HBox entryHboxHelper(Label label, Node field) {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(label, field);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private static ToolBar getToolbar( ObservableList<EntryView> tableData, Entry entry, KdbxObject kdbxObject) {
        ToolBar toolBar = new ToolBar();
        toolBar.setMinWidth(minWidth + 12);
        toolBar.setOrientation(Orientation.VERTICAL);

        // Save
        Button saveBtn = Buttons.getCheckBtn("Save", minWidth);
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setOnAction(e -> {
            try {
                saveEntry(entry, kdbxObject);
            } catch (IOException e1) {
                e1.printStackTrace();
                errorMsgSave();
            }
        });

        // Delete Entry
        FontIcon deleteIcon = new FontIcon("fa-trash");
        Button deleteBtn = new Button("Delete", deleteIcon);
        deleteBtn.setMinWidth(minWidth);
        deleteBtn.setOnAction(e -> deleteEntryAndExit(tableData, entry, kdbxObject));

        // Spacer
        Region spacer = Buttons.getSpacerVGrow();

        // Exit
        FontIcon exitIcon = new FontIcon("fa-close");
        exitIcon.setIconColor(Color.RED);
        Button exitBtn = new Button("Exit", exitIcon);
        exitBtn.setMinWidth(minWidth);
        exitBtn.setOnAction(e -> window.setScene(prevScene));

        toolBar.getItems().addAll(saveBtn, deleteBtn, spacer, exitBtn);
        return toolBar;
    }

    private static void deleteEntryAndExit(ObservableList<EntryView> tableData, Entry entry, KdbxObject kdbxObject) {
        if (DialogConfirm.display(window, String.format("Delete %s?", entry.getTitle()))) {
            window.setScene(prevScene);
            Group parent = entry.getParent();
            parent.removeEntry(entry);
            try {
                KdbxOps.saveKdbx(kdbxObject);
            } catch (IOException e1) {
                e1.printStackTrace();
                errorMsgSave();
            }
            AppHome.updateTableData(tableData, AppHome.currentGroup);
        }
    }

    private static void errorMsgSave() {
        DialogAlert.display(window, "Error!", "Error saving change to database!");
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
