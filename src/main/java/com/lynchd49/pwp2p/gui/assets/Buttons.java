package com.lynchd49.pwp2p.gui.assets;

import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;


public class Buttons {
    public static Button getCheckBtn(String text, double width) {
        return getButton(text, width, "fa-check", Color.GREEN);
    }

    public static Button getCloseBtn(String text, double width) {
        return getButton(text, width, "fa-close", Color.RED);
    }

    public static Button getStartBtn(String text, double width) {
        return getButton(text, width, "fa-play", Color.GREEN);
    }

    public static Button getStopBtn(String text, double width) {
        return getButton(text, width, "fa-stop", Color.RED);
    }

    public static Button getConnectBtn(String text, double width) {
        return getButton(text, width, "fa-link", Color.GREEN);
    }

    @NotNull
    private static Button getButton(String text, double width, String s, Color red) {
        FontIcon closeIcon = new FontIcon(s);
        closeIcon.setIconColor(red);
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
