package com.lynchd49.pwp2p.utils

import org.linguafranca.pwdb.Database
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxObjectTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static String dbPath1 = dir + "test1.kdbx"
    final static File dbFile1 = new File(dbPath1)
    final static String dbPw1 = "test1"

    final static String dbPath2 = dir + "test2.kdbx"
    final static File dbFile2 = new File(dbPath2)
    final static String dbPw2 = "test2"


    def "Get Database #name from KdbxObject"() {
        when:
        Database db = KdbxOps.loadKdbx(file, password)
        KdbxObject kdbxObj = new KdbxObject(db, path, password)
        Database dbCopy = kdbxObj.getDatabase()

        then:
        dbCopy == db

        where:
        name    | path    | file    | password
        "test1" | dbPath1 | dbFile1 | dbPw1
        "test2" | dbPath2 | dbFile2 | dbPw2
    }

    def "Get path #path from KdbxObject"() {
        when:
        Database db = KdbxOps.loadKdbx(file, password)
        KdbxObject kdbxObj = new KdbxObject(db, path, password)
        String pathCopy = kdbxObj.getPath()

        then:
        pathCopy == path

        where:
        path    | file    | password
        dbPath1 | dbFile1 | dbPw1
        dbPath2 | dbFile2 | dbPw2
    }

    def "Get file name #fileName from KdbxObject"() {
        when:
        Database db = KdbxOps.loadKdbx(file, password)
        KdbxObject kdbxObj = new KdbxObject(db, path, password)
        String nameCopy = kdbxObj.getFileName()

        then:
        nameCopy == name

        where:
        name         | path    | file    | password
        "test1.kdbx" | dbPath1 | dbFile1 | dbPw1
        "test2.kdbx" | dbPath2 | dbFile2 | dbPw2
    }

    def "Get credentials #password from KdbxObject"() {
        when:
        Database db = KdbxOps.loadKdbx(file, password)
        KdbxObject kdbxObj = new KdbxObject(db, path, password)
        String passwordCopy = kdbxObj.getCredentials()

        then:
        passwordCopy == password

        where:
        path    | file    | password
        dbPath1 | dbFile1 | dbPw1
        dbPath2 | dbFile2 | dbPw2
    }
}
