import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOperationsTest extends Specification {

    final static String dbPath1 = "test1.kdbx"
    final static String dbPw1 = "test1"

    final static String dbPath2 = "test2.kdbx"
    final static String dbPw2 = "test2"

    final static String wrongPath = "fake.kdbx"
    final static String wrongPw = "wrong password"

    /**
     * Load Operation Tests
     */

    def "Should load #path with credentials: #creds"() {
        when:
        KdbxOperations.loadKdbx(path, creds)

        then:
        notThrown(IllegalStateException)

        where:
        path    || creds
        dbPath1 || dbPw1
        dbPath2 || dbPw2
    }

    def "Should not load #path with credentials: #creds"() {
        when:
        KdbxOperations.loadKdbx(path, creds)

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
        wrongPath   || dbPw1   || IllegalStateException || "Incorrect credentials or invalid file."
        wrongPath   || wrongPw || IllegalStateException || "Incorrect credentials or invalid file."
        wrongPath   || null    || NullPointerException  || "File path or credentials are null."

    }

    /**
     * Save Operation Tests
     */

    def "Should save to a new database"() {
        when:
        KdbxOperations.saveKdbx(path, creds)

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
