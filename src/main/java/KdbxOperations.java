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
}
