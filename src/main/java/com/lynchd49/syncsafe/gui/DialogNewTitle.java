package com.lynchd49.syncsafe.gui;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

class DialogNewTitle {
    static Optional<String> display(String itemType) {
        TextInputDialog dialog = new TextInputDialog(String.format("New %s", itemType));
        dialog.setTitle(String.format("Create New %s", itemType));
        dialog.setHeaderText("New " + itemType);
        dialog.setContentText(String.format("Please enter a title for the new %s:", itemType));
        return dialog.showAndWait();
    }
}
