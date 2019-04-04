package com.lynchd49.pwp2p.gui.assets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
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
}
