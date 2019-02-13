package com.lynchd49.syncsafe.utils;

import org.linguafranca.pwdb.*;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class KdbxOperations {

    private static SimpleDatabase getDatabase() {
        return new SimpleDatabase();
    }

    private static Database loadDatabase(Credentials credentials, InputStream inputStream) {
        try {
            return SimpleDatabase.load(credentials, inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Load Operations
     */

    public static Database loadKdbx(String path, String creds) throws IOException {
        try (InputStream inputStream = KdbxOperations.class.getClassLoader().getResourceAsStream(path)) {
            Credentials credentials = new KdbxCreds(creds.getBytes());
            return loadDatabase(credentials, inputStream);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Incorrect credentials or invalid file.");
        } catch (NullPointerException e) {
            throw new NullPointerException("File path or credentials are null.");
        }
    }

    /**
     * Save Operations
     */

    private static Entry entryFactory(Database database, String s, int e) {
        return database.newEntry(String.format("Group %s Entry %d", s, e));
    }

    public static void saveKdbx(String path, String creds) throws IOException {
        Database database = getDatabase();
        for (int g = 0; g < 5; g++) {
            Group group = database.getRootGroup().addGroup(database.newGroup(Integer.toString(g)));
            for (int e = 0; e <= g; e++) {
                // entry factory is a local helper to populate an entry
                group.addEntry(entryFactory(database, Integer.toString(g), e));
            }

        }
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            database.save(new KdbxCreds(creds.getBytes()), outputStream);
        }
    }
}
