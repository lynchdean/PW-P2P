package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.EntryView;
import com.lynchd49.syncsafe.utils.KdbxTreeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;

import java.util.List;

class AppHome {

    // Window & Scenes
    private static Stage window;

    private static ObservableList<EntryView> tableData;

    static Scene loadScene(Stage stage, Database db) {
        window = stage;

        setTableData(db.getRootGroup());
        TreeView<String> treeView = KdbxTreeUtils.getTreeView(db);

        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<String> treeItemPath = KdbxTreeUtils.getTreeItemPath(newValue);
                    Group g = KdbxTreeUtils.getGroupFromPath(db, treeItemPath);
                    updateTableData(g);
                });

        TableView<EntryView> table = new TableView<>();

        TableColumn<EntryView, String> titleCol = new TableColumn<>("Title");
        titleCol.setMinWidth(160);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<EntryView, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(160);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<EntryView, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(160);
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        table.setItems(tableData);
        table.getColumns().addAll(titleCol, usernameCol, passwordCol);

        HBox panesHb = new HBox();
        panesHb.getChildren().addAll(treeView, table);

        return new Scene(panesHb, 800, 400);
    }

    private static void setTableData(Group g) {
        ObservableList<EntryView> entryViewList = getObsList(g);
        tableData = entryViewList;
    }

    private static ObservableList<EntryView> getObsList(Group g) {
        ObservableList<EntryView> entryViewList = FXCollections.observableArrayList();
        if (g.getEntriesCount() > 0) {
            for (Object entryObj : g.getEntries()) {
                Entry entry = (Entry) entryObj;
                EntryView entryView = new EntryView(entry);
                entryViewList.add(entryView);
            }
        }
        return entryViewList;
    }

    private static void updateTableData(Group g) {
        ObservableList<EntryView> entryViewList = getObsList(g);
        tableData.removeAll(tableData);
        tableData.addAll(entryViewList);
    }
}
