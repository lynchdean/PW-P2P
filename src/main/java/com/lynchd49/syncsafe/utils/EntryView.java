package com.lynchd49.syncsafe.utils;

import javafx.beans.property.SimpleStringProperty;
import org.linguafranca.pwdb.Entry;

public class EntryView {

    private final SimpleStringProperty title;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;

    public EntryView(Entry e) {
        this.title = new SimpleStringProperty(e.getTitle());
        this.username = new SimpleStringProperty(e.getUsername());
        this.password = new SimpleStringProperty(e.getPassword());
    }

    public String getTitle() {
        return title.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }
}
