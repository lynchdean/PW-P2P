package com.lynchd49.syncsafe.utils

import org.linguafranca.pwdb.Database
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOpsTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static String dbPath1 = dir + "test1.kdbx"
    final static File dbFile1 = new File(dir + "test1.kdbx")
    final static String dbPw1 = "test1"

    final static String dbPath2 = dir + "test2.kdbx"
    final static File dbFile2 = new File(dbPath2)
    final static String dbPw2 = "test2"

    final static File wrongFile = new File(dir + "wrongFile.kdbx")
    final static String wrongPw = "wrong password"

    /**
     * Load Operation Tests
     */

    def "Should load #file with credentials: #creds"() {
        when:
        KdbxOps.loadKdbx(file, creds)

        then:
        notThrown(IllegalStateException)

        where:
        file    || creds
        dbFile1 || dbPw1
        dbFile2 || dbPw2
    }

    def "Should not load #file with credentials: #creds"() {
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
        Database database = KdbxOps.loadKdbx(new File(dbPath), creds)
        KdbxObject kdbxObject = new KdbxObject(database, path, creds)
        KdbxOps.saveKdbx(kdbxObject)

        then:
        def file = new File(path)
        file.exists()

        cleanup:
        file.delete()

        where:
        dbPath  || path         || creds
        dbPath1 || "test1.kdbx" || dbPw1
        dbPath2 || "test2.kdbx" || dbPw2
    }

    def "Should save and change database password: #path"() {
        when:
        Database database = KdbxOps.loadKdbx(new File(dbPath), creds)
        KdbxObject kdbxObject = new KdbxObject(database, path, newCreds)
        KdbxOps.saveKdbx(kdbxObject)

        then:
        def file = new File(path)
        file.exists()
        KdbxOps.loadKdbx(file, newCreds)

        cleanup:
        file.delete()

        where:
        dbPath  || path         || creds    || newCreds
        dbPath1 || "test1.kdbx" || dbPw1    || "new1"
        dbPath2 || "test2.kdbx" || dbPw2    || "new2"
    }


    def "Should save and change database password, and check old password doesn't work: #newPath"() {
        when:
        // Save database
        Database database = KdbxOps.loadKdbx(new File(dbPath), creds)
        KdbxObject kdbxObject = new KdbxObject(database, newPath, newCreds)
        KdbxOps.saveKdbx(kdbxObject)

        // Try to access database with old password
        def newFile = new File(newPath)
        KdbxOps.loadKdbx(newFile, creds)

        then:
        def ex = thrown(IllegalStateException)
        ex.message == expectedMsg

        cleanup:
        newFile.delete()

        where:
        dbPath  || newPath      || creds    || newCreds || expectedMsg
        dbPath1 || "test1.kdbx" || dbPw1    || "new1"   || "Incorrect credentials or invalid file."
        dbPath2 || "test2.kdbx" || dbPw2    || "new2"   || "Incorrect credentials or invalid file."
    }
}
