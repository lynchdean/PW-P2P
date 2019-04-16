package com.lynchd49.pwp2p.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(true);
        primaryStage.setTitle("PW-P2P");
        Scene fileChooserScene = FileChooser.loadScene(primaryStage);
        primaryStage.setScene(fileChooserScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}