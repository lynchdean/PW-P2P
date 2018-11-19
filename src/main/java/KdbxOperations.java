
import org.junit.Test;
import org.linguafranca.pwdb.*;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.InputStream;


public abstract class KdbxOperations {

    /*
     * Load .kdbx file
     */
//    public void loadKdbx(String kdbxPath, String credInput) throws IOException {
    public void loadKdbx() throws Exception {
        KdbxCreds credentials = new KdbxCreds("hunter2".getBytes());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.kdbx");
        Database database = SimpleDatabase.load(credentials, inputStream);
        database.visit(new Visitor.Print());
    }
}
