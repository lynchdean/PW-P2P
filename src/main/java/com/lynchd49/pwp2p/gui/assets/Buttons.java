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

    public static Button getCopyButton() {
        FontIcon copyIcon = new FontIcon("fa-copy");
        return new Button("", copyIcon);
    }

    public static Button getVisibilityButton() {
        FontIcon eyeIcon = new FontIcon("fa-eye");
        return new Button("", eyeIcon);
    }

    @NotNull
    private static Button getButton(String text, double width, String s, Color red) {
        FontIcon fontIcon = new FontIcon(s);
        fontIcon.setIconColor(red);
        Button button = new Button(text, fontIcon);
        button.setMinWidth(width);
        return button;
    }

    public static Region getSpacerVGrow() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        return spacer;
    }
}
