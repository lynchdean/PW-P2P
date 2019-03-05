package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.KdbxTreeUtils;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;

class AppHome {

    private static Text actionStatus;

    // Window & Scenes
    private static Stage window;

    static Scene loadScene(Stage stage, Database db) {
        window = stage;

        TreeView treeView = KdbxTreeUtils.getTreeView(db);

        HBox panesHb = new HBox();
        panesHb.getChildren().addAll(treeView);

        return new Scene(panesHb, 800, 400);
    }
}
