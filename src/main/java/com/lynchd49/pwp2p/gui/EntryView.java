package com.lynchd49.pwp2p.gui;

import com.lynchd49.pwp2p.gui.assets.Buttons;
import com.lynchd49.pwp2p.gui.assets.Dialogs;
import com.lynchd49.pwp2p.utils.EntryTableView;
import com.lynchd49.pwp2p.utils.KdbxObject;
import com.lynchd49.pwp2p.utils.KdbxOps;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

class EntryView {

    private static Stage window;
    private static Scene prevScene;

    private static TextField titleField;
    private static TextField usernameField;
    private static TextField passwordField;
    private static TextField urlField;
    private static TextArea notesArea;

    private static boolean passwordVisible = false;

    private final static double minWidth = 80;

    static Scene loadScene(Stage stage, ObservableList<EntryTableView> tableData, Entry entry, KdbxObject kdbxObject) {
        window = stage;
        prevScene = window.getScene();

        // Title
        Label titleLabel = new Label("Title:");
        entryLabelHelper(titleLabel);
        titleField = new TextField(entry.getTitle());
        HBox.setHgrow(titleField, Priority.ALWAYS);
        Button copyTitleBtn = Buttons.getClipboardButton();
        copyButtonHelper(copyTitleBtn, titleField);
        HBox titleHbox = entryHboxHelper(titleLabel, titleField, copyTitleBtn);

        // Username
        Label usernameLabel = new Label("Username:");
        entryLabelHelper(usernameLabel);
        usernameField = new TextField(entry.getUsername());
        HBox.setHgrow(usernameField, Priority.ALWAYS);
        Button copyUsernameBtn = Buttons.getClipboardButton();
        copyButtonHelper(copyUsernameBtn, usernameField);
        HBox usernameHbox = entryHboxHelper(usernameLabel, usernameField, copyUsernameBtn);

        // Password
        Label passwordLabel = new Label("Password:");
        entryLabelHelper(passwordLabel);
        String passwordHidden = getObscuredPw(entry.getPassword());
        passwordField = new TextField(passwordHidden);
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        Button visibilityBtn = Buttons.getVisibilityButton();
        visibilityBtnHelper(visibilityBtn, entry);
        Button copyPasswordBtn = Buttons.getClipboardButton();
        copyButtonHelper(copyPasswordBtn, passwordField);
        Button changePasswordBtn = Buttons.getWrenchButton();
        changePwBtnHelper(changePasswordBtn, kdbxObject, entry);
        HBox passwordHbox = entryHboxHelper(passwordLabel, passwordField, changePasswordBtn, visibilityBtn, copyPasswordBtn);

        // URL
        Label urlLabel = new Label("URL:");
        entryLabelHelper(urlLabel);
        urlField = new TextField(entry.getUrl());
        HBox.setHgrow(urlField, Priority.ALWAYS);
        Button copyUrlBtn = Buttons.getClipboardButton();
        copyButtonHelper(copyUrlBtn, urlField);
        HBox urlHbox = entryHboxHelper(urlLabel, urlField, copyUrlBtn);

        // Expires
        Label expiresLabel = new Label("Expires:");
        entryLabelHelper(expiresLabel);
        TextField expiresField = new TextField(entry.getExpiryTime().toString());
        HBox.setHgrow(expiresField, Priority.ALWAYS);
        expiresField.setDisable(true);
        HBox expiresHbox = entryHboxHelper(expiresLabel, expiresField);

        // Notes
        Label notesLabel = new Label("Notes:");
        entryLabelHelper(notesLabel);
        notesArea = new TextArea(entry.getNotes());
        HBox.setHgrow(notesArea, Priority.ALWAYS);
        Button copyNotesBtn = Buttons.getClipboardButton();
        copyButtonHelper(copyNotesBtn, notesArea);
        HBox notesHbox = entryHboxHelper(notesLabel, notesArea, copyNotesBtn);

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

    private static void changePwBtnHelper(Button button, KdbxObject kdbxObject, Entry entry) {
        button.setOnAction(e -> Dialogs.displayNewPassword(window, kdbxObject, entry));
    }

    private static void visibilityBtnHelper(Button button, Entry entry) {
        button.setOnAction(e -> {
            if (!passwordVisible) {
                Button button1 = (Button) e.getSource();
                button1.setGraphic(new FontIcon("fa-eye-slash"));
                passwordField.setText(entry.getPassword());
            } else {
                Button button1 = (Button) e.getSource();
                button1.setGraphic(new FontIcon("fa-eye"));
                passwordField.setText(getObscuredPw(entry.getPassword()));
            }
            passwordVisible = !passwordVisible;
        });
    }

    private static String getObscuredPw(String password) {
        char bullet = '\u2022';
        return String.valueOf(bullet).repeat(password.length());
    }

    private static void copyButtonHelper(Button button, TextField textField) {
        button.setOnAction(e -> {
            String toCopy = textField.getText();
            copyStringToClipboard(toCopy);
        });
    }

    private static void copyButtonHelper(Button button, TextArea textArea) {
        button.setOnAction(e -> {
            String toCopy = textArea.getText();
            copyStringToClipboard(toCopy);
        });
    }

    private static void copyStringToClipboard(String toCopy) {
        StringSelection stringSelection = new StringSelection(toCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private static void entryLabelHelper(Label titleLabel) {
        titleLabel.setMinWidth(minWidth);
        titleLabel.setAlignment(Pos.CENTER_RIGHT);
    }

    @NotNull
    private static HBox entryHboxHelper(Label label, Node field, Button ... buttons) {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(label, field);
        for(Button button : buttons) {
            hbox.getChildren().add(button);
        }
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    @NotNull
    private static HBox entryHboxHelper(Label label, Node field) {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(label, field);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private static ToolBar getToolbar(ObservableList<EntryTableView> tableData, Entry entry, KdbxObject kdbxObject) {
        ToolBar toolBar = new ToolBar();
        toolBar.setMinWidth(minWidth + 12);
        toolBar.setOrientation(Orientation.VERTICAL);

        // Save
        Button saveBtn = Buttons.getSaveBtn("Save", minWidth);
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setOnAction(e -> saveEntry(entry, kdbxObject));

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

    private static void deleteEntryAndExit(ObservableList<EntryTableView> tableData, Entry entry, KdbxObject kdbxObject) {
        if (Dialogs.displayConfirm(window, String.format("Delete %s?", entry.getTitle()))) {
            window.setScene(prevScene);
            Group parent = entry.getParent();
            parent.removeEntry(entry);
            KdbxOps.saveKdbx(kdbxObject);
            Home.updateTableData(tableData, Home.currentGroup);
        }
    }

    private static void errorMsgSave() {
        Dialogs.displayAlert(window, "Error!", "Error saving change to database!");
    }

    private static void saveEntry(Entry entry, KdbxObject kdbxObject) {
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
