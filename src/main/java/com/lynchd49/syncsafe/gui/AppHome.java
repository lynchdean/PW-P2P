package com.lynchd49.syncsafe.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;

class AppHome {

    private static Text actionStatus;

    // Window & Scenes
    private static Stage window;

    static Scene loadScene(Stage stage, Database db) {
        window = stage;

        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList ("A", "B", "C", "D");
        listView.setItems(items);

        StackPane root = new StackPane();
        root.getChildren().add(listView);

        return new Scene(root, 800, 400);
    }
}
