package com.lynchd49.pwp2p.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.*;


public class KdbxOps {

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static Database loadDatabase(Credentials credentials, InputStream inputStream) {
        try {
            return SimpleDatabase.load(credentials, inputStream);
        } catch (Exception e) {
            LOGGER.error("Error loading database.", e);
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
            LOGGER.error("Incorrect credentials or invalid file.", e);
            throw new IllegalStateException();
        } catch (NullPointerException e) {
            LOGGER.error("File or credentials are null.", e);
            throw new NullPointerException();
        } catch (FileNotFoundException e) {
            LOGGER.error("File is missing or has been deleted.", e);
            throw new FileNotFoundException();
        }
    }

    /**
     * Save Operations
     */

    public static void saveKdbx(KdbxObject kdbxObject) {
        try (FileOutputStream outputStream = new FileOutputStream(kdbxObject.getPath())) {
            kdbxObject.getDatabase().save(new KdbxCreds(kdbxObject.getCredentials().getBytes()), outputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("File not found: %s", kdbxObject.getPath()), e);
        } catch (IOException e) {
            LOGGER.error(String.format("Error saving file: %s", kdbxObject.getPath()), e);
        }
    }
}
