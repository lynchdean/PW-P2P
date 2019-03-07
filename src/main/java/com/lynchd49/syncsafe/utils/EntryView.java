package com.lynchd49.syncsafe.utils;

import javafx.beans.property.SimpleStringProperty;
import org.linguafranca.pwdb.Entry;

public class EntryView {

    private final SimpleStringProperty title;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password = new SimpleStringProperty("\u2022".repeat(5));
    private final SimpleStringProperty url;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty expires;
    private final SimpleStringProperty created;
    private final SimpleStringProperty modified;
    private final SimpleStringProperty accessed;

    public EntryView(Entry e) {
        this.title = new SimpleStringProperty(e.getTitle());
        this.username = new SimpleStringProperty(e.getUsername());
//        this.password = new SimpleStringProperty(e.getPassword());
        this.url = new SimpleStringProperty(e.getUrl());
        this.notes = new SimpleStringProperty(e.getNotes());
        this.expires = new SimpleStringProperty(Boolean.toString(e.getExpires()));
        this.created = new SimpleStringProperty(e.getCreationTime().toString());
        this.modified = new SimpleStringProperty(e.getLastModificationTime().toString());
        this.accessed = new SimpleStringProperty(e.getLastAccessTime().toString());
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

    public String getUrl() {
        return url.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public String getExpires() {
        return expires.get();
    }

    public String getCreated() {
        return created.get();
    }

    public String getModified() {
        return modified.get();
    }

    public String getAccessed() {
        return accessed.get();
    }
}
