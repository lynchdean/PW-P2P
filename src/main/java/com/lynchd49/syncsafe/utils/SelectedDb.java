package com.lynchd49.syncsafe.utils;

import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Group;

public class SelectedDb {
    private Database db;

    SelectedDb(Database database) {
        this.db = database;
    }

    public Group getRootGroup() {
        return db.getRootGroup();
    }
}
