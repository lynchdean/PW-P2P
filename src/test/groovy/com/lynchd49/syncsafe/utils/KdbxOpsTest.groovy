package com.lynchd49.syncsafe.utils

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOpsTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static File dbFile1 = new File(dir + "test1.kdbx")
    final static String dbPw1 = "test1"

    final static File dbFile2 = new File(dir + "test2.kdbx")
    final static String dbPw2 = "test2"

    final static File wrongFile = new File(dir + "fake.kdbx")
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
        dbFile1 || dbPw1
        dbFile2 || dbPw2
    }

    def "Should not load #path with credentials: #creds"() {
        when:
        KdbxOps.loadKdbx(file, creds)

        then:
        def ex = thrown(expectedException)
        ex.message == expectedMsg

        where:
        file      || creds   || expectedException     || expectedMsg
        dbFile1   || wrongPw || IllegalStateException || "Incorrect credentials or invalid file."
        dbFile2   || wrongPw || IllegalStateException || "Incorrect credentials or invalid file."
        null      || wrongPw || NullPointerException  || "File or credentials are null."
        dbFile1   || null    || NullPointerException  || "File or credentials are null."
        null      || null    || NullPointerException  || "File or credentials are null."
        wrongFile || dbPw1   || FileNotFoundException || "File is missing or has been deleted."
        wrongFile || wrongPw || FileNotFoundException || "File is missing or has been deleted."
        wrongFile || null    || FileNotFoundException || "File is missing or has been deleted."

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
