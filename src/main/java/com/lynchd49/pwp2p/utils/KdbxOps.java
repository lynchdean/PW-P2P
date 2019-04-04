package com.lynchd49.pwp2p.utils;

import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.*;


public class KdbxOps {

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

    public static Database loadKdbx(File file, String creds) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            Credentials credentials = new KdbxCreds(creds.getBytes());
            return loadDatabase(credentials, inputStream);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Incorrect credentials or invalid file.");
        } catch (NullPointerException e) {
            throw new NullPointerException("File or credentials are null.");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File is missing or has been deleted.");
        }
    }

    /**
     * Save Operations
     */

    public static void saveKdbx(KdbxObject kdbxObject) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(kdbxObject.getPath())) {
            kdbxObject.getDatabase().save(new KdbxCreds(kdbxObject.getCredentials().getBytes()), outputStream);
        }
    }
}
