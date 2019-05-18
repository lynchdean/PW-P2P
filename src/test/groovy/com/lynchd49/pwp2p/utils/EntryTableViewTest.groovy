package com.lynchd49.pwp2p.utils

import org.linguafranca.pwdb.Database
import org.linguafranca.pwdb.Entry
import spock.lang.Specification

class EntryTableViewTest extends Specification {

    final static String dir = System.getProperty("user.dir") + "/src/test/resources/"

    final static String dbPath1 = dir + "test1.kdbx"
    final static File dbFile1 = new File(dbPath1)
    final static String dbPw1 = "test1"

    Database db = KdbxOps.loadKdbx(dbFile1, dbPw1)
    Entry entry = (Entry) db.getRootGroup().getEntries().get(0)

    EntryTableView etv = new EntryTableView(entry)

    def "Get title from Entry table view"() {
        when:
        String title = etv.getTitle()

        then:
        title == "root entry 1"
    }

    def "Get username from Entry table view"() {
        when:
        String username = etv.getUsername()

        then:
        username == "username 1"
    }

    def "Get password from Entry table view"() {
        when:
        String password = etv.getPassword()

        then:
        password == "•••••"
    }

    def "Get URL from Entry table view"() {
        when:
        String url = etv.getUrl()

        then:
        url == "www.dcu.ie"
    }

    def "Get notes from Entry table view"() {
        when:
        String notes = etv.getNotes()

        then:
        notes == "Don't edit this Entry! Used in testing!"
    }

    def "Get expiry from Entry table view"() {
        when:
        String expiry = etv.getExpires()

        then:
        expiry
    }

    def "GetCreated"() {
    }

    def "Get created from Entry table view"() {
        when:
        String created = etv.getCreated()

        then:
        created == "Thu Feb 21 15:30:17 GMT 2019"
    }
}
