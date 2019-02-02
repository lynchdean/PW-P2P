import org.linguafranca.pwdb.*;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.InputStream;


public abstract class KdbxOperations {

    /*
     * Load .kdbx file
     */
//    public void loadKdbx(String kdbxPath, String credInput) throws IOException {
    public void loadKdbx(String path, String creds) throws Exception {
        KdbxCreds credentials = new KdbxCreds(creds.getBytes());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        Database database = SimpleDatabase.load(credentials, inputStream);
        database.visit(new Visitor.Print());
    }

    public boolean isCreds(String path, String creds) {
        KdbxCreds credentials = new KdbxCreds(creds.getBytes());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        Database database;
        try {
            database = SimpleDatabase.load(credentials, inputStream);
        } catch (Exception e) {
            return false;
        }
        database.visit(new Visitor.Print());
        return true;
    }
}
