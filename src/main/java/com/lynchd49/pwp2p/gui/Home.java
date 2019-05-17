package com.lynchd49.pwp2p.gui;

import com.lynchd49.pwp2p.gui.assets.Buttons;
import com.lynchd49.pwp2p.gui.assets.Dialogs;
import com.lynchd49.pwp2p.server.SyncClient;
import com.lynchd49.pwp2p.server.SyncServer;
import com.lynchd49.pwp2p.utils.EntryTableView;
import com.lynchd49.pwp2p.utils.KdbxObject;
import com.lynchd49.pwp2p.utils.KdbxOps;
import com.lynchd49.pwp2p.utils.KdbxTreeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
        TableView<EntryTableView> table = new TableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);

        addColumn(table, "Title", "title");
        addColumn(table, "Username", "username");
        addColumn(table, "Password", "password");
        addColumn(table, "URL", "url");
        addColumn(table, "Notes", "notes");
        addColumn(table, "Expires", "expires");
        addColumn(table, "Created", "created");
        addColumn(table, "Modified", "modified");
        addColumn(table, "Accessed", "accessed");

        ObservableList<EntryTableView> tableData = getObsList(db.getRootGroup());
        table.setItems(tableData);
        table.setRowFactory(tv -> {
            TableRow<EntryTableView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EntryTableView rowData = row.getItem();
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
        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setCenter(table);
        MenuBar menuBar = getMenuBar(window, treeView, tableData, kdbxObject);
        borderPaneMain.setTop(menuBar);
        borderPaneMain.setLeft(treeView);

        Tab tabMain = new Tab();
        tabMain.setText("Password Management");
        tabMain.setGraphic(new FontIcon("fa-lock"));
        tabMain.setContent(borderPaneMain);

        // Sync layout (Right main tab)
        TabPane syncSideTabs = getSyncTabs(window, kdbxObject);
        BorderPane borderPaneSync = new BorderPane();
        borderPaneSync.setCenter(syncSideTabs);
        ToolBar syncToolBar = new ToolBar();
        syncToolBar.setMinHeight(30);
        borderPaneSync.setTop(syncToolBar);
        borderPaneSync.setRight(getMachineInfo());

        Tab tabSync = new Tab();
        tabSync.setText("Synchronisation");
        tabSync.setGraphic(new FontIcon("fa-exchange"));
        tabSync.setContent(borderPaneSync);

        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(tabMain, tabSync);
        tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            tabPane.setTabMinWidth(tabPane.getWidth() / 2 - 23);
            tabPane.setTabMaxWidth(tabPane.getWidth() / 2 - 23);
        });

        return new Scene(tabPane, 800, 400);
    }

    @NotNull
    private static TabPane getSyncTabs(Stage window, KdbxObject kdbxObject) {
        TabPane syncTabPane = new TabPane();
        syncTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab serverTab = getSendTab(window, kdbxObject);
        serverTab.setGraphic(new FontIcon("fa-upload"));
        Tab clientTab = getReceiveTab(window, kdbxObject);
        clientTab.setGraphic(new FontIcon("fa-download"));
        syncTabPane.getTabs().addAll(serverTab, clientTab);
        syncTabPane.setSide(Side.LEFT);
        syncTabPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            syncTabPane.setTabMaxWidth(syncTabPane.getHeight() / 2 - 23);
            syncTabPane.setTabMinWidth(syncTabPane.getHeight() / 2 - 23);
        });
        return syncTabPane;
    }

    @NotNull
    private static HBox getMachineInfo() throws UnknownHostException {
        Label titleLabel = new Label("This machine:");
        Separator hSeparator = new Separator();
        Label hostnameLabel = new Label("Hostname:");
        Text hostnameText = new Text(InetAddress.getLocalHost().getHostName());
        HBox hostnameHbox = new HBox(10);
        hostnameHbox.getChildren().addAll(hostnameLabel, hostnameText);
        hostnameHbox.setAlignment(Pos.CENTER_LEFT);

        Label hostAddrLabel = new Label("Host Address:");
        Text hostAddrText = new Text(InetAddress.getLocalHost().getHostAddress());
        HBox hostAddrHbox = new HBox(10);
        hostAddrHbox.getChildren().addAll(hostAddrLabel, hostAddrText);
        hostAddrHbox.setAlignment(Pos.CENTER_LEFT);

        Separator vSeparator = new Separator();
        vSeparator.setOrientation(Orientation.VERTICAL);
        VBox infoVbox = new VBox(10);
        infoVbox.getChildren().addAll(titleLabel, hSeparator, hostnameHbox, hostAddrHbox);
        HBox machineInfoHbox = new HBox(10);
        machineInfoHbox.getChildren().addAll(vSeparator, infoVbox);
        machineInfoHbox.setPadding(new Insets(25, 25, 25, 25));
        return machineInfoHbox;
    }

    @NotNull
    private static Tab getSendTab(Stage window, KdbxObject kdbxObject) {
        double minWidth = labelMinWidth + 60;

        // Recipient Hostname
        Label hostLabel = new Label("Recipient Hostname:");
        hostLabel.setMinWidth(minWidth);
        hostLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField hostField = new TextField();
        HBox hostHbox = new HBox(10);
        hostHbox.getChildren().addAll(hostLabel, hostField);

        // Port
        Label portLabel = new Label("Port:");
        portLabel.setMinWidth(minWidth);
        portLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField portField = new TextField("4444");
        portField.setMaxWidth(60);
        applyPortInputLimits(portField);
        HBox portHbox = new HBox(10);
        portHbox.getChildren().addAll(portLabel, portField);

        // Run
        Label runLabel = new Label("Start Server:");
        runLabel.setMinWidth(minWidth);
        runLabel.setAlignment(Pos.CENTER_RIGHT);
        Button startServerBtn = Buttons.getStartBtn("Run", 60);
        startServerBtn.setDisable(false);
        startServerBtn.setOnAction(e -> startServer(window, portField.getText(), hostField.getText(), kdbxObject));
        HBox btnHbox = new HBox(10);
        btnHbox.setPadding(new Insets(20, 0, 0, 0));
        btnHbox.getChildren().addAll(runLabel, startServerBtn);

        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hostHbox, portHbox, btnHbox);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        Tab serverTab = new Tab("Send");
        serverTab.setContent(vbox);
        return serverTab;
    }

    private static void startServer(Stage window, String portString, String recipientHostname, KdbxObject kdbxObject) {
        int portInt = Integer.parseInt(portString);
        if (portInt >= 0 && portInt <= 65535) {
            SyncServer server = new SyncServer(portInt, recipientHostname, kdbxObject.getPath());
            server.start();
            Dialogs.displayServerStatus(window, server);
        } else {
            Dialogs.displayAlert(window, "Invalid Port Number", "Please enter a valid port. (0-65535)");
        }
    }

    @NotNull
    private static Tab getReceiveTab(Stage window, KdbxObject kdbxObject) {
        double minWidth = labelMinWidth + 60;

        // Sender Hostname
        Label hostLabel = new Label("Sender Hostname:");
        hostLabel.setMinWidth(minWidth);
        hostLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField hostField = new TextField();
        HBox hostHbox = new HBox(10);
        hostHbox.getChildren().addAll(hostLabel, hostField);

        // Port
        Label portLabel = new Label("Server Port:");
        portLabel.setMinWidth(minWidth);
        portLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField portField = new TextField("4444");
        portField.setMaxWidth(60);
        applyPortInputLimits(portField);
        HBox portHbox = new HBox(10);
        portHbox.getChildren().addAll(portLabel, portField);

        // File name
        Label nameLabel = new Label("Save as:");
        nameLabel.setMinWidth(minWidth);
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        TextField nameField = new TextField();
        Label appendLabel = new Label(".kdbx");
        HBox nameHbox = new HBox(10);
        nameHbox.getChildren().addAll(nameLabel, nameField, appendLabel);

        // Buttons
        Label runLabel = new Label("Start Client:");
        runLabel.setMinWidth(minWidth);
        runLabel.setAlignment(Pos.CENTER_RIGHT);
        Button startClientBtn = Buttons.getStartBtn("Run", 60);
        startClientBtn.setOnAction(e -> startClient(window, hostField.getText(), portField.getText(), kdbxObject.getPath()));
        HBox btnHbox = new HBox(10);
        btnHbox.setPadding(new Insets(20, 0, 0, 0));
        btnHbox.getChildren().addAll(runLabel, startClientBtn);

        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hostHbox, portHbox, nameHbox, btnHbox);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        Tab clientTab = new Tab("Receive");
        clientTab.setContent(vbox);
        return clientTab;
    }

    private static void applyPortInputLimits(TextField portField) {
        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only allow numeric characters in port field
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            // Limit the number of characters to 5
            int maxLength = 5;
            if (portField.getText().length() > maxLength) {
                portField.setText(portField.getText().substring(0, maxLength));
            }
        });
    }

    private static void startClient(Stage window, String hostname, String portString, String outputFilePath) {
        if (validHostname(hostname) || hostname.equals("localhost")) {
            int portInt = Integer.parseInt(portString);
            if (portInt >= 0 && portInt <= 65535) {
                SyncClient client = new SyncClient();
                client.start(hostname, portInt, outputFilePath);
                Dialogs.displayClientStatus(window, client);
            }
        } else {
            Dialogs.displayAlert(window, "Invalid Hostname", "Please enter a valid hostname.");
        }
    }

    private static boolean validHostname(String hostname) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return hostname.matches(PATTERN);
    }

    private static void addColumn(TableView table, String title, String propertyVal) {
        TableColumn<EntryTableView, String> col = new TableColumn<>(title);
        col.setMinWidth(colMinWidth);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyVal));
        table.getColumns().add(col);
    }

    static void updateTableData(ObservableList<EntryTableView> tableData, Group g) {
        tableData.clear();
        tableData.addAll(getObsList(g));
    }

    private static ObservableList<EntryTableView> getObsList(Group g) {
        ObservableList<EntryTableView> entryViewList = FXCollections.observableArrayList();
        if (g.getEntriesCount() > 0) {
            for (Object entryObj : g.getEntries()) {
                Entry entry = (Entry) entryObj;
                EntryTableView entryView = new EntryTableView(entry);
                entryViewList.add(entryView);
            }
        }
        return entryViewList;
    }

    private static void showEntryScene(Stage window, ObservableList<EntryTableView> tableData, String entryTitle, KdbxObject kdbxObject) {
        List entries = currentGroup.getEntries();
        Entry entry = null;
        for (Object entryObj : entries) {
            Entry e = (Entry) entryObj;
            if (e.getTitle().equals(entryTitle)) {
                entry = e;
            }
        }
        if (entry == null) {
            Dialogs.displayAlert(window, "Error", "Error retrieving entry.");
        } else {
            Scene entryScene = EntryView.loadScene(window, tableData, entry, kdbxObject);
            window.setScene(entryScene);
        }
    }

    private static MenuBar getMenuBar(Stage window, TreeView<String> treeView, ObservableList<EntryTableView> tableData, KdbxObject kdbxObject) {
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
        newEntryItem.setOnAction(e -> newEntry(tableData, kdbxObject));
        entryMenu.getItems().addAll(newEntryItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(dbMenu, groupMenu, entryMenu);
        return menuBar;
    }

    // Create a new group in the currently selected group
    private static void newGroup(Stage window, TreeView<String> treeView, ObservableList<EntryTableView> tableData, KdbxObject kdbxObject) {
        Optional<String> result = Dialogs.displayNewTitle("Group");
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
                KdbxOps.saveKdbx(kdbxObject);
                updateTableData(tableData, currentGroup);
                updateTreeView(treeView, kdbxObject);
            } else {
                Dialogs.displayAlert(window, "Error Creating Group!", "A child group with that name already exists!");
            }
        });
    }

    // Create a new entry in the currently selected group
    private static void newEntry(ObservableList<EntryTableView> tableData, KdbxObject kdbxObject) {
        Optional<String> result = Dialogs.displayNewTitle("Entry");
        result.ifPresent(s -> {
            Entry entry = kdbxObject.getDatabase().newEntry(s);
            currentGroup.addEntry(entry);
        });
        KdbxOps.saveKdbx(kdbxObject);
        updateTableData(tableData, currentGroup);
    }

    // Delete the currently selected group
    private static void deleteCurrentGroup(Stage window, TreeView<String> treeView, KdbxObject kdbxObject) {
        if (!currentGroup.equals(kdbxObject.getDatabase().getRootGroup())) {
            if (Dialogs.displayConfirm(window, String.format("Delete %s?", currentGroup.getName()))) {
                Group groupToRemove = currentGroup;
                currentGroup = currentGroup.getParent();
                currentGroup.removeGroup(groupToRemove);
            }
        } else {
            Dialogs.displayAlert(window, "Error: Root group!", "Cannot delete root group!");
        }
        KdbxOps.saveKdbx(kdbxObject);
        updateTreeView(treeView, kdbxObject);
    }

    private static void updateTreeView(TreeView<String> treeView, KdbxObject kdbxObject) {
        treeView.getRoot().getChildren().clear();
        TreeItem<String> root = KdbxTreeUtils.getTreeRoot(kdbxObject.getDatabase());
        treeView.setRoot(root);
    }
}
