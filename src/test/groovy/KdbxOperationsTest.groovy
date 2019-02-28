import spock.lang.Specification
import spock.lang.Unroll
import com.lynchd49.syncsafe.utils.KdbxOps

@Unroll
class KdbxOperationsTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static String dbPath1 = dir + "test1.kdbx"
    final static String dbPw1 = "test1"

    final static String dbPath2 = dir + "test2.kdbx"
    final static String dbPw2 = "test2"

    final static String wrongPath = dir + "fake.kdbx"
    final static String wrongPw = "wrong password"

    /**
     * Load Operation Tests
     */

    def "Should load #path with credentials: #creds"() {
        when:
        KdbxOps.loadKdbx(path, creds)

        then:
        notThrown(IllegalStateException)

        where:
        path    || creds
        dbPath1 || dbPw1
        dbPath2 || dbPw2
    }

    def "Should not load #path with credentials: #creds"() {
        when:
        KdbxOps.loadKdbx(path, creds)

        then:
        def ex = thrown(expectedException)
        ex.message == expectedMsg

        where:
        path        || creds   || expectedException     || expectedMsg
        dbPath1     || wrongPw || IllegalStateException || "Incorrect credentials or invalid file."
        dbPath2     || wrongPw || IllegalStateException || "Incorrect credentials or invalid file."
        null        || wrongPw || NullPointerException  || "File path or credentials are null."
        dbPath1     || null    || NullPointerException  || "File path or credentials are null."
        null        || null    || NullPointerException  || "File path or credentials are null."
        wrongPath   || dbPw1   || FileNotFoundException || "File is missing or has been deleted."
        wrongPath   || wrongPw || FileNotFoundException || "File is missing or has been deleted."
        wrongPath   || null    || FileNotFoundException || "File is missing or has been deleted."

    }

    /**
     * Save Operation Tests
     */

    def "Should save to a new database: #path"() {
        when:
        KdbxOps.saveKdbx(path, creds)

        then:
        def file = new File(path)
        file.exists()

        cleanup:
        file.delete()

        where:
        path         || creds
        "test1.kdbx" || dbPw1
        "test2.kdbx" || dbPw2
    }
}
