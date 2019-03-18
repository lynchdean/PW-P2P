package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.EntryView;
import com.lynchd49.syncsafe.utils.KdbxTreeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

    private static Group currentGroup;

    static Scene loadScene(Stage stage, Database db) {
        window = stage;
        currentGroup = db.getRootGroup();

        setTableData(db.getRootGroup());
        TreeView<String> treeView = KdbxTreeUtils.getTreeView(db);
        treeView.setMaxWidth(200);

        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<String> treeItemPath = KdbxTreeUtils.getTreeItemPath(newValue);
                    Group g = KdbxTreeUtils.getGroupFromPath(db, treeItemPath);
                    currentGroup = g;
                    updateTableData(g);
                });

        TableView<EntryView> table = new TableView<>();
        table.setMaxWidth(600);

        TableColumn<EntryView, String> titleCol = new TableColumn<>("Title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<EntryView, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(100);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<EntryView, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(100);
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<EntryView, String> urlCol = new TableColumn<>("URL");
        urlCol.setMinWidth(100);
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<EntryView, String> notesCol = new TableColumn<>("Notes");
        notesCol.setMinWidth(100);
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        TableColumn<EntryView, String> expiresCol = new TableColumn<>("Expires");
        expiresCol.setMinWidth(100);
        expiresCol.setCellValueFactory(new PropertyValueFactory<>("expires"));

        TableColumn<EntryView, String> createdCol = new TableColumn<>("Created");
        createdCol.setMinWidth(100);
        createdCol.setCellValueFactory(new PropertyValueFactory<>("created"));

        TableColumn<EntryView, String> modifiedCol = new TableColumn<>("Modified");
        modifiedCol.setMinWidth(100);
        modifiedCol.setCellValueFactory(new PropertyValueFactory<>("modified"));

        TableColumn<EntryView, String> accessedCol = new TableColumn<>("Accessed");
        accessedCol.setMinWidth(100);
        accessedCol.setCellValueFactory(new PropertyValueFactory<>("accessed"));

        table.setItems(tableData);
        table.getColumns().addAll(titleCol, usernameCol, passwordCol, urlCol, notesCol, expiresCol, createdCol, modifiedCol, accessedCol);

        table.setRowFactory(tv -> {
            TableRow<EntryView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    EntryView rowData = row.getItem();
                    showEntryScene(rowData.getTitle());
                }
            });
            return row;
        });

        HBox panesHb = new HBox();
        panesHb.getChildren().addAll(treeView, table);

        return new Scene(panesHb, 800, 400);
    }

    private static void setTableData(Group g) {
        tableData = getObsList(g);
    }

    private static void updateTableData(Group g) {
        tableData.clear();
        tableData.addAll(getObsList(g));
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

    private static void showEntryScene(String entryTitle) {
        List entries = currentGroup.getEntries();
        Entry entry = null;
        for (Object entryObj : entries) {
            Entry e = (Entry) entryObj;
            if (e.getTitle().equals(entryTitle)) {
                entry = e;
            }
        }
        if (entry == null) {
            AlertBox.display(window, "Error", "Error retrieving entry.");
        }
        else {
            Scene entryScene = AppEntryView.loadScene(window, entry);
            window.setScene(entryScene);
        }
    }
}
