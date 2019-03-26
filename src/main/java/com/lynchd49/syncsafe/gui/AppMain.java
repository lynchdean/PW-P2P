package com.lynchd49.syncsafe.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    private static final String titleText = "SyncSafe";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(true);
        primaryStage.setTitle(titleText);
        Scene fileChooserScene = AppFileChooser.loadScene(primaryStage);
        primaryStage.setScene(fileChooserScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }
}