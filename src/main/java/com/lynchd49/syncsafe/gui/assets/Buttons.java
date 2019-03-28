package com.lynchd49.syncsafe.gui.assets;

import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;


public class Buttons {
    public static Button getCheckBtn(String text, double width) {
        FontIcon checkIcon = new FontIcon("fa-check");
        checkIcon.setIconColor(Color.GREEN);
        Button checkBtn = new Button(text, checkIcon);
        checkBtn.setMinWidth(width);
        return checkBtn;
    }

    public static Button getCloseBtn(String text, double width) {
        FontIcon closeIcon = new FontIcon("fa-close");
        closeIcon.setIconColor(Color.RED);
        Button closeBtn = new Button(text, closeIcon);
        closeBtn.setMinWidth(width);
        return closeBtn;
    }

    public static Region getSpacerVGrow() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        return spacer;
    }
}
