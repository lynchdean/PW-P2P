package com.lynchd49.syncsafe.gui;

import com.lynchd49.syncsafe.utils.EntryView;
import com.lynchd49.syncsafe.utils.KdbxObject;
import com.lynchd49.syncsafe.utils.KdbxOps;
import com.lynchd49.syncsafe.utils.KdbxTreeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

class AppHome {

    // Window & Scenes
    private static Stage window;

    private static TreeView<String> treeView;
    private static ObservableList<EntryView> tableData;
    private static KdbxObject kdbxObject;
    private static Group currentGroup;


    static Scene loadScene(Stage stage, KdbxObject kdbxObj) {
        window = stage;
        kdbxObject = kdbxObj;
        Database db = kdbxObject.getDatabase();
        currentGroup = db.getRootGroup();

        setTableData(db.getRootGroup());
        treeView = KdbxTreeUtils.getTreeView(db);
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
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EntryView rowData = row.getItem();
                    showEntryScene(rowData.getTitle(), kdbxObject);
                }
            });
            return row;
        });

        HBox mainHbox = new HBox();
        mainHbox.getChildren().addAll(treeView, table);
        MenuBar menuBar = getMenuBar();
        VBox mainVbox = new VBox();
        mainVbox.getChildren().addAll(menuBar, mainHbox);

        return new Scene(mainVbox, 800, 400);
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

    private static void showEntryScene(String entryTitle, KdbxObject kdbxObject) {
        List entries = currentGroup.getEntries();
        Entry entry = null;
        for (Object entryObj : entries) {
            Entry e = (Entry) entryObj;
            if (e.getTitle().equals(entryTitle)) {
                entry = e;
            }
        }
        if (entry == null) {
            DialogAlert.display(window, "Error", "Error retrieving entry.");
        } else {
            Scene entryScene = AppEntryView.loadScene(window, entry, kdbxObject);
            window.setScene(entryScene);
        }
    }

    private static MenuBar getMenuBar() {
        // Database Items
        Menu dbMenu = new Menu("Database");
        MenuItem openItem = new MenuItem("Open database");
        openItem.setOnAction(e -> System.out.println("Open"));
        // TODO open AppFileChooser
        MenuItem closeItem = new MenuItem("Close database");
        closeItem.setOnAction(e -> System.out.println("Close"));
        // TODO close db & return to AppFileChooser
        dbMenu.getItems().addAll(openItem, closeItem);

        // Group Items
        Menu groupMenu = new Menu("Groups");
        MenuItem newGroupItem = new MenuItem("New group");
        newGroupItem.setOnAction(e -> newGroup());
        MenuItem deleteGroupItem = new MenuItem("Delete current group");
        deleteGroupItem.setOnAction(e -> deleteCurrentGroup());
        groupMenu.getItems().addAll(newGroupItem, deleteGroupItem);

        // Entry Items
        Menu entryMenu = new Menu("Entries");
        MenuItem newEntryItem = new MenuItem("New entry");
        newEntryItem.setOnAction(e -> newEntry());
        entryMenu.getItems().addAll(newEntryItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(dbMenu, groupMenu, entryMenu);
        return menuBar;
    }

    // Create a new group in the currently selected group
    private static void newGroup() {
        Optional<String> result = DialogNewTitle.display("Group");
        result.ifPresent(s -> {
            Group group = kdbxObject.getDatabase().newGroup(s);
            currentGroup.addGroup(group);
            updateTableData(currentGroup);
        });
        try {
            KdbxOps.saveKdbx(kdbxObject);
        } catch (IOException e) {
            e.printStackTrace();
            DialogAlert.display(window, "Error!", "Error saving change to database!");
        }
        treeView = KdbxTreeUtils.getTreeView(kdbxObject.getDatabase());
    }

    // Create a new entry in the currently selected group
    private static void newEntry() {
        Optional<String> result = DialogNewTitle.display("Entry");
        result.ifPresent(s -> {
            Entry entry = kdbxObject.getDatabase().newEntry(s);
            currentGroup.addEntry(entry);
        });
        try {
            KdbxOps.saveKdbx(kdbxObject);
        } catch (IOException e) {
            e.printStackTrace();
            DialogAlert.display(window, "Error!", "Error saving change to database!");
        }
        updateTableData(currentGroup);
    }

    // Delete the currently selected group
    private static void deleteCurrentGroup() {
        if (!currentGroup.equals(kdbxObject.getDatabase().getRootGroup())) {
            if (DialogConfirm.display(window, String.format("Delete %s?", currentGroup.getName()))) {
                Group groupToRemove = currentGroup;
                currentGroup = currentGroup.getParent();
                currentGroup.getParent().removeGroup(groupToRemove);
            }
        } else {
            DialogAlert.display(window, "Error: Root group!", "Cannot delete root group!");
        }
        try {
            KdbxOps.saveKdbx(kdbxObject);
        } catch (IOException e) {
            e.printStackTrace();
            DialogAlert.display(window, "Error!", "Error saving change to database!");
        }
    }
}
