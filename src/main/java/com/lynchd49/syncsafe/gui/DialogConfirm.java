package com.lynchd49.syncsafe.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

class DialogConfirm {

    static boolean display(Stage ownerWindow, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(ownerWindow);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(msg);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }
}
