import org.linguafranca.pwdb.*;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class KdbxOperations {

    private SimpleDatabase getDatabase() {
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

    public static Database loadKdbx(String path, String creds) {
        try (InputStream inputStream = KdbxOperations.class.getClassLoader().getResourceAsStream(path)) {
            Credentials credentials = new KdbxCreds(creds.getBytes());
            return loadDatabase(credentials, inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid credentials.");
        }
    }

    /**
     * Save Operations
     */

    private Entry entryFactory(Database database, String s, int e) {
        return database.newEntry(String.format("Group %s Entry %d", s, e));
    }

    public void saveKdbx() throws IOException {
        // create an empty database
        Database database = getDatabase();
        // add some groups and entries
        for (int g = 0; g < 5; g++) {
            Group group = database.getRootGroup().addGroup(database.newGroup(Integer.toString(g)));
            for (int e = 0; e <= g; e++) {
                // entry factory is a local helper to populate an entry
                group.addEntry(entryFactory(database, Integer.toString(g), e));
            }
        }
        // save to a file with password "123"
        try (FileOutputStream outputStream = new FileOutputStream("testOutput/test.kdbx")) {
            database.save(new KdbxCreds("123".getBytes()), outputStream);
        }
    }
}
