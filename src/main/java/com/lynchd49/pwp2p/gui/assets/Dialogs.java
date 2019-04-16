package com.lynchd49.pwp2p.gui.assets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class Dialogs {
    public static void displayAlert(Stage ownerWindow, String title, String msg) {
        Stage alertWindow = new Stage();
        alertWindow.initOwner(ownerWindow);
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);

        Text message = new Text(msg);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> alertWindow.close());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(message, closeBtn);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }

    public static boolean displayConfirm(Stage ownerWindow, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(ownerWindow);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(msg);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    public static Optional<String> displayNewTitle(String itemType) {
        TextInputDialog dialog = new TextInputDialog(String.format("New %s", itemType));
        dialog.setTitle(String.format("Create New %s", itemType));
        dialog.setHeaderText("New " + itemType);
        dialog.setContentText(String.format("Please enter a title for the new %s:", itemType));
        return dialog.showAndWait();
    }

    public static void displayNewPassword(Stage ownerWindow) {
        Stage alertWindow = new Stage();
        alertWindow.initOwner(ownerWindow);
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Generate New Password");

        // Checkboxes
        CheckBox alphaUpperCb = new CheckBox("A-Z");
        alphaUpperCb.setSelected(true);
        CheckBox alphaLowerCb = new CheckBox("a-z");
        alphaLowerCb.setSelected(true);
        CheckBox numericCb = new CheckBox("0-9");
        numericCb.setSelected(true);
        CheckBox specialCb = new CheckBox("!\"#...");

        Label spinnerLabel = new Label("Length: ");
        Spinner<Integer> lengthSpinner = new Spinner<>(1, 999, 1);
        lengthSpinner.getValueFactory().setValue(16);
        lengthSpinner.setMaxWidth(70);
        HBox spinnerHbox = new HBox(10);
        spinnerHbox.setAlignment(Pos.CENTER_LEFT);
        spinnerHbox.getChildren().addAll(spinnerLabel, lengthSpinner);

        Region hSpacer = Buttons.getSpacerHGrow();
        HBox optionsHbox = new HBox(10);
        optionsHbox.setAlignment(Pos.CENTER_LEFT);
        optionsHbox.getChildren().addAll(alphaUpperCb, alphaLowerCb, numericCb, specialCb, hSpacer, spinnerHbox);

        Button generateBtn = Buttons.getGenerateBtn("Generate", 80);
        generateBtn.setAlignment(Pos.CENTER);
        generateBtn.setOnAction(e -> {
            // TODO add functionality
            System.out.println("Generate");
        });

        TextField newPwField = new TextField();
        newPwField.setMinWidth(400);
        HBox.setHgrow(newPwField, Priority.ALWAYS);
        HBox generateHbox = new HBox(10);
        generateHbox.getChildren().addAll(generateBtn, newPwField);

        // Save & Cancel Buttons
        Button saveBtn = Buttons.getSaveBtn("Save", 80);
        saveBtn.setOnAction(e -> {
            // TODO add functionality
            System.out.println("save");
            alertWindow.close();
        });
        Button cancelBtn = Buttons.getCloseBtn("Cancel", 80);
        cancelBtn.setOnAction(e -> alertWindow.close());
        HBox btnHbox = new HBox(10);
        btnHbox.getChildren().addAll(saveBtn, cancelBtn);
        btnHbox.setAlignment(Pos.BOTTOM_RIGHT);

        // Main Layout
        VBox mainVbox = new VBox(20);
        mainVbox.setPadding(new Insets(25, 50, 25, 50));
        mainVbox.getChildren().addAll(generateHbox, optionsHbox, btnHbox);
        Scene scene = new Scene(mainVbox);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
