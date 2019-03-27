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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

class AppHome {

    private final static double colMinWidth = 100;
//    private static ObservableList<EntryView> tableData;
    static Group currentGroup;

    static Scene loadScene(Stage window, KdbxObject kdbxObject) {
        Database db = kdbxObject.getDatabase();
        currentGroup = db.getRootGroup();

        // Table
        TableView<EntryView> table = new TableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);

        addColumn(table,"Title", "title");
        addColumn(table,"Username", "username");
        addColumn(table,"Password", "password");
        addColumn(table,"URL", "url");
        addColumn(table,"Notes", "notes");
        addColumn(table,"Expires", "expires");
        addColumn(table,"Created", "created");
        addColumn(table,"Modified", "modified");
        addColumn(table,"Accessed", "accessed");

        ObservableList<EntryView> tableData = getObsList(db.getRootGroup());
        table.setItems(tableData);
        table.setRowFactory(tv -> {
            TableRow<EntryView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EntryView rowData = row.getItem();
                    showEntryScene(window, tableData, rowData.getTitle(), kdbxObject);
                }
            });
            return row;
        });

        TreeView<String> treeView = new TreeView<>(KdbxTreeUtils.getTreeRoot(db));
        treeView.setMaxWidth(200);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<String> treeItemPath = KdbxTreeUtils.getTreeItemPath(newValue);
                    Group g = KdbxTreeUtils.getGroupFromPath(db, treeItemPath);
                    currentGroup = g;
                    updateTableData(tableData, g);
                });

        // Main layout
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(table);
        MenuBar menuBar = getMenuBar(window, treeView, tableData, kdbxObject);
        mainPane.setTop(menuBar);
        mainPane.setLeft(treeView);

        return new Scene(mainPane, 800, 400);
    }

    private static void addColumn(TableView table, String title, String propertyVal) {
        TableColumn<EntryView, String> col = new TableColumn<>(title);
        col.setMinWidth(colMinWidth);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyVal));
        table.getColumns().add(col);
    }

    static void updateTableData(ObservableList<EntryView> tableData, Group g) {
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

    private static void showEntryScene(Stage window,  ObservableList<EntryView> tableData, String entryTitle, KdbxObject kdbxObject) {
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
            Scene entryScene = AppEntryView.loadScene(window, tableData, entry, kdbxObject);
            window.setScene(entryScene);
        }
    }

    private static MenuBar getMenuBar(Stage window, TreeView<String> treeView, ObservableList<EntryView> tableData, KdbxObject kdbxObject) {
        FontIcon plusIcon = new FontIcon("fa-plus");
        plusIcon.setIconColor(Color.GREEN);
        FontIcon minusIcon = new FontIcon("fa-minus");
        minusIcon.setIconColor(Color.RED);
        FontIcon closeIcon = new FontIcon("fa-close");
        closeIcon.setIconColor(Color.RED);

        // Database Items
        Menu dbMenu = new Menu("Database");
        MenuItem quitItem = new MenuItem("Quit", closeIcon);
        quitItem.setOnAction(e -> window.close());
        dbMenu.getItems().addAll(quitItem);

        // Group Items
        Menu groupMenu = new Menu("Groups");
        MenuItem newGroupItem = new MenuItem("New group", plusIcon);
        newGroupItem.setOnAction(e -> newGroup(window, treeView, tableData, kdbxObject));
        MenuItem deleteGroupItem = new MenuItem("Delete current group", minusIcon);
        deleteGroupItem.setOnAction(e -> deleteCurrentGroup(window, treeView, kdbxObject));
        groupMenu.getItems().addAll(newGroupItem, deleteGroupItem);

        // Entry Items
        Menu entryMenu = new Menu("Entries");
        MenuItem newEntryItem = new MenuItem("New entry", plusIcon);
        newEntryItem.setOnAction(e -> newEntry(window, tableData, kdbxObject));
        entryMenu.getItems().addAll(newEntryItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(dbMenu, groupMenu, entryMenu);
        return menuBar;
    }

    // Create a new group in the currently selected group
    private static void newGroup(Stage window, TreeView<String> treeView, ObservableList<EntryView> tableData, KdbxObject kdbxObject) {
        Optional<String> result = DialogNewTitle.display("Group");
        result.ifPresent(s -> {
            Group group = kdbxObject.getDatabase().newGroup(s);
            boolean found = false;
            for (Object childObj : currentGroup.getGroups()) {
                Group childGroup = (Group) childObj;
                if (childGroup.getName().equals(s)) {
                    found = true;
                }
            }
            if (!found) {
                currentGroup.addGroup(group);
                try {
                    KdbxOps.saveKdbx(kdbxObject);
                    updateTableData(tableData, currentGroup);
                    updateTreeView(treeView, kdbxObject);
                } catch (IOException e) {
                    e.printStackTrace();
                    errorMsgSave(window);
                }
            } else {
                DialogAlert.display(window, "Error Creating Group!", "A child group with that name already exists!");
            }
        });
    }

    // Create a new entry in the currently selected group
    private static void newEntry(Stage window,  ObservableList<EntryView> tableData, KdbxObject kdbxObject) {
        Optional<String> result = DialogNewTitle.display("Entry");
        result.ifPresent(s -> {
            Entry entry = kdbxObject.getDatabase().newEntry(s);
            currentGroup.addEntry(entry);
        });
        try {
            KdbxOps.saveKdbx(kdbxObject);
        } catch (IOException e) {
            e.printStackTrace();
            errorMsgSave(window);
        }
        updateTableData(tableData, currentGroup);
    }

    // Delete the currently selected group
    private static void deleteCurrentGroup(Stage window, TreeView<String> treeView, KdbxObject kdbxObject) {
        if (!currentGroup.equals(kdbxObject.getDatabase().getRootGroup())) {
            if (DialogConfirm.display(window, String.format("Delete %s?", currentGroup.getName()))) {
                Group groupToRemove = currentGroup;
                currentGroup = currentGroup.getParent();
                currentGroup.removeGroup(groupToRemove);
            }
        } else {
            DialogAlert.display(window, "Error: Root group!", "Cannot delete root group!");
        }
        try {
            KdbxOps.saveKdbx(kdbxObject);
            updateTreeView(treeView, kdbxObject);
        } catch (IOException e) {
            e.printStackTrace();
            errorMsgSave(window);
        }
    }

    private static void errorMsgSave(Stage window) {
        DialogAlert.display(window, "Error!", "Error saving change to database!");
    }

    private static void updateTreeView(TreeView<String> treeView, KdbxObject kdbxObject) {
        treeView.getRoot().getChildren().clear();
        TreeItem<String> root = KdbxTreeUtils.getTreeRoot(kdbxObject.getDatabase());
        treeView.setRoot(root);
    }
}
