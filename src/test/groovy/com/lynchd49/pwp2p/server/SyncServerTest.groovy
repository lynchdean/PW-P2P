package com.lynchd49.pwp2p.server


import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SyncServerTest extends Specification {
    String testFilePath = "test1.kdbx"
    int portNumber = 4444
    String recipient = "localhost"
    SyncServer server

    def "Should fail given negative port number: #portNum"() {
        when:
        server = new SyncServer(portNum, recipient, testFilePath)
        server.start()

        then:
        def ex = thrown(expectedException)
        ex.message == String.format("%s%s", expectedMsg, portNum)

        where:
        portNum || expectedException        || expectedMsg
        -1      || IllegalArgumentException || "Port value out of range: "
        -100    || IllegalArgumentException || "Port value out of range: "
        -999    || IllegalArgumentException || "Port value out of range: "
        -9999   || IllegalArgumentException || "Port value out of range: "
    }

    def "Should fail given too high port number: #portNum"() {
        when:
        server = new SyncServer(portNum, recipient, testFilePath)
        server.start()

        then:
        def ex = thrown(expectedException)
        ex.message == String.format("%s%s", expectedMsg, portNum)

        where:
        portNum || expectedException        || expectedMsg
        65536   || IllegalArgumentException || "Port value out of range: "
        99999   || IllegalArgumentException || "Port value out of range: "
    }
}