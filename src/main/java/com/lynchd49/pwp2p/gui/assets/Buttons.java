package com.lynchd49.pwp2p.gui.assets;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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

    public static Button getSaveBtn(String text, double width) {
        return getButton(text, width, "fa-save", Color.GREEN);
    }

    public static Button getGenerateBtn(String text, double width) {
        return getButton(text, width, "fa-random", Color.CORNFLOWERBLUE);
    }

    public static Button getClipboardButton() {
        return getIconButton("fa-clipboard");
    }

    public static Button getVisibilityButton() {
        return getIconButton("fa-eye");
    }

    public static Button getWrenchButton() {
        return getIconButton("fa-wrench");
    }

    @NotNull
    private static Button getButton(String text, double width, String s, Color red) {
        FontIcon fontIcon = new FontIcon(s);
        fontIcon.setIconColor(red);
        Button button = new Button(text, fontIcon);
        button.setMinWidth(width);
        return button;
    }

    @NotNull
    private static Button getIconButton(String s) {
        FontIcon eyeIcon = new FontIcon(s);
        return new Button("", eyeIcon);
    }

    public static Region getSpacerVGrow() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        return spacer;
    }

    public static Region getSpacerHGrow() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        return spacer;
    }
}
