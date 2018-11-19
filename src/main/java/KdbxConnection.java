import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;

import java.io.InputStream;

public class KdbxConnection {

    public void openConnection(String kdbxPath, String credInput) {
        KdbxCreds credentials = new KdbxCreds((credInput).getBytes());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(kdbxPath);

        try {
            Database database = SimpleDatabase.load(credentials, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Incorrect password.");
        }
    }
}
