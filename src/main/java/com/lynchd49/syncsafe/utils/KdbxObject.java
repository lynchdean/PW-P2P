package com.lynchd49.syncsafe.utils;

import org.linguafranca.pwdb.Database;

public class KdbxObject {

    private Database database;
    private String path;
    private String credentials;

    public KdbxObject(Database db, String filePath, String creds) {
        this.database = db;
        this.path = filePath;
        this.credentials = creds;
    }

    public Database getDatabase() {
        return database;
    }

    String getPath() {
        return path;
    }

    String getCredentials() {
        return credentials;
    }
}
