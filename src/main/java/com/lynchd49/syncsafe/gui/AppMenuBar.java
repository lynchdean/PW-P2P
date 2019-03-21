package com.lynchd49.syncsafe.gui;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

class AppMenuBar {

    static MenuBar getMenuBar() {

        Menu dbMenu = new Menu("Database");
        MenuItem openItem = new MenuItem("Open database");
        openItem.setOnAction(e -> System.out.println("Open"));
        MenuItem closeItem = new MenuItem("Close database");
        closeItem.setOnAction(e -> System.out.println("Close"));
        dbMenu.getItems().addAll(openItem, closeItem);

        Menu groupMenu = new Menu("Groups");

        Menu entryMenu = new Menu("Entries");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(dbMenu, groupMenu, entryMenu);

        return menuBar;
    }
}
