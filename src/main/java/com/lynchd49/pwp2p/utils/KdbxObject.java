package com.lynchd49.pwp2p.utils;

import org.linguafranca.pwdb.Database;

public class KdbxObject {

    final private Database database;
    final private String path;
    final private String credentials;

    public KdbxObject(Database db, String filePath, String creds) {
        this.database = db;
        this.path = filePath;
        this.credentials = creds;
    }

    public Database getDatabase() {
        return database;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return path.substring(path.lastIndexOf("/"));
    }

    String getCredentials() {
        return credentials;
    }
}
