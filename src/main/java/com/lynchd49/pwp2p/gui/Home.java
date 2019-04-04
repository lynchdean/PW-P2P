package com.lynchd49.pwp2p.gui;

import com.lynchd49.pwp2p.gui.assets.Buttons;
import com.lynchd49.pwp2p.utils.KdbxObject;
import com.lynchd49.pwp2p.utils.KdbxOps;
import com.lynchd49.pwp2p.utils.KdbxTreeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

class Home {

    private final static double colMinWidth = 100;
    private final static double labelMinWidth = 80;

    static Group currentGroup;

    static Scene loadScene(Stage window, KdbxObject kdbxObject) throws UnknownHostException {
        Database db = kdbxObject.getDatabase();
        currentGroup = db.getRootGroup();

        // Table
        TableView<com.lynchd49.pwp2p.utils.EntryView> table = new TableView<>();
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

        ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData = getObsList(db.getRootGroup());
        table.setItems(tableData);
        table.setRowFactory(tv -> {
            TableRow<com.lynchd49.pwp2p.utils.EntryView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    com.lynchd49.pwp2p.utils.EntryView rowData = row.getItem();
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

        // Main layout (Left main tab)
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(table);
        MenuBar menuBar = getMenuBar(window, treeView, tableData, kdbxObject);
        mainPane.setTop(menuBar);
        mainPane.setLeft(treeView);
        Tab tabMain = new Tab();
        tabMain.setText("Password Management");
        tabMain.setContent(mainPane);

        // Sync layout (Right main tab)
        TabPane syncTabPane = new TabPane();
        Tab serverTab = getServerTab();
        Tab clientTab = getClientTab();
        syncTabPane.getTabs().addAll(serverTab, clientTab);

        Tab tabSync = new Tab();
        tabSync.setText("Synchronisation");
        tabSync.setContent(syncTabPane);

        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(tabMain, tabSync);

        return new Scene(tabPane, 800, 400);
    }

    @NotNull
    private static Tab getServerTab() throws UnknownHostException {
        // Hostname
        Label hostLabel = new Label("Hostname:");
        hostLabel.setMinWidth(labelMinWidth);
        hostLabel.setAlignment(Pos.CENTER_RIGHT);
        Text hostText = new Text(InetAddress.getLocalHost().getHostAddress());
        HBox hostHbox = new HBox(10);
        hostHbox.getChildren().addAll(hostLabel, hostText);

        // Port
        Label portLabel = new Label("Port:");
        portLabel.setMinWidth(labelMinWidth);
        portLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField portField = new TextField("4444");
        portField.setMaxWidth(60);
        HBox portHbox = new HBox(10);
        portHbox.getChildren().addAll(portLabel, portField);

        // Buttons
        Button startServerBtn = Buttons.getStartBtn("Start Server", 100);
        // TODO add button functionality
        Button stopServerBtn = Buttons.getStopBtn("Stop Server", 100);
        // TODO add button functionality
        HBox btnHbox = new HBox(10);
        btnHbox.setPadding(new Insets(20, 0, 0, 0));
        btnHbox.getChildren().addAll(startServerBtn, stopServerBtn);

        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hostHbox, portHbox, btnHbox);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        Tab serverTab = new Tab("Host connection");
        serverTab.setContent(vbox);
        return serverTab;
    }

    @NotNull
    private static Tab getClientTab() {
        // Hostname
        Label hostLabel = new Label("Server Hostname:");
        hostLabel.setMinWidth(labelMinWidth + 40);
        hostLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField hostField = new TextField();
        HBox hostHbox = new HBox(10);
        hostHbox.getChildren().addAll(hostLabel, hostField);

        // Port
        Label portLable = new Label("Server Port:");
        portLable.setMinWidth(labelMinWidth + 40);
        portLable.setAlignment(Pos.CENTER_RIGHT);
        TextField portField = new TextField("4444");
        portField.setMaxWidth(60);
        HBox portHbox = new HBox(10);
        portHbox.getChildren().addAll(portLable, portField);

        // Buttons
        Button connectServerBtn = Buttons.getConnectBtn("Start Connection", 100);
        // TODO add button functionality
        Button stopConnectionBtn = Buttons.getStopBtn("Stop Connection", 100);
        // TODO add button functionality
        HBox btnHbox = new HBox(10);
        btnHbox.setPadding(new Insets(20, 0, 0, 0));
        btnHbox.getChildren().addAll(connectServerBtn, stopConnectionBtn);

        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hostHbox, portHbox, btnHbox);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        Tab clientTab = new Tab("Connect to another host");
        clientTab.setContent(vbox);
        return clientTab;
    }

    private static void addColumn(TableView table, String title, String propertyVal) {
        TableColumn<com.lynchd49.pwp2p.utils.EntryView, String> col = new TableColumn<>(title);
        col.setMinWidth(colMinWidth);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyVal));
        table.getColumns().add(col);
    }

    static void updateTableData(ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData, Group g) {
        tableData.clear();
        tableData.addAll(getObsList(g));
    }

    private static ObservableList<com.lynchd49.pwp2p.utils.EntryView> getObsList(Group g) {
        ObservableList<com.lynchd49.pwp2p.utils.EntryView> entryViewList = FXCollections.observableArrayList();
        if (g.getEntriesCount() > 0) {
            for (Object entryObj : g.getEntries()) {
                Entry entry = (Entry) entryObj;
                com.lynchd49.pwp2p.utils.EntryView entryView = new com.lynchd49.pwp2p.utils.EntryView(entry);
                entryViewList.add(entryView);
            }
        }
        return entryViewList;
    }

    private static void showEntryScene(Stage window, ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData, String entryTitle, KdbxObject kdbxObject) {
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
            Scene entryScene = EntryView.loadScene(window, tableData, entry, kdbxObject);
            window.setScene(entryScene);
        }
    }

    private static MenuBar getMenuBar(Stage window, TreeView<String> treeView, ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData, KdbxObject kdbxObject) {
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
    private static void newGroup(Stage window, TreeView<String> treeView, ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData, KdbxObject kdbxObject) {
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
    private static void newEntry(Stage window, ObservableList<com.lynchd49.pwp2p.utils.EntryView> tableData, KdbxObject kdbxObject) {
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
