package com.lynchd49.pwp2p.utils

import org.linguafranca.pwdb.Database
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOpsTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static String dbPath1 = dir + "test1.kdbx"
    final static File dbFile1 = new File(dbPath1)
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
        thrown(expectedException)

        where:
        file      || creds   || expectedException
        dbFile1   || wrongPw || IllegalStateException
        dbFile2   || wrongPw || IllegalStateException
        null      || wrongPw || NullPointerException
        dbFile1   || null    || NullPointerException
        null      || null    || NullPointerException
        wrongFile || dbPw1   || FileNotFoundException
        wrongFile || wrongPw || FileNotFoundException
        wrongFile || null    || FileNotFoundException

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
        // Save receivedDatabase
        Database database = KdbxOps.loadKdbx(new File(dbPath), creds)
        KdbxObject kdbxObject = new KdbxObject(database, newPath, newCreds)
        KdbxOps.saveKdbx(kdbxObject)

        // Try to access receivedDatabase with old password
        def newFile = new File(newPath)
        KdbxOps.loadKdbx(newFile, creds)

        then:
        thrown(IllegalStateException)

        cleanup:
        newFile.delete()

        where:
        dbPath  || newPath      || creds    || newCreds
        dbPath1 || "test1.kdbx" || dbPw1    || "new1"
        dbPath2 || "test2.kdbx" || dbPw2    || "new2"
    }
}
