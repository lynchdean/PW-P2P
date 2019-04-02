package com.lynchd49.syncsafe.server


import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SyncServerTest extends Specification {

    int portNumber = 4444
    SyncServer server

    def setup() {
        server = new SyncServer()
    }

    def "Should fail given negative port number: #portNumber"() {
        when:
        server.start(portNum)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == String.format("%s%s", expectedMsg, portNum)

        where:
        portNum || expectedException        || expectedMsg
        -1      || IllegalArgumentException || "Port value out of range: "
        -100    || IllegalArgumentException || "Port value out of range: "
        -999    || IllegalArgumentException || "Port value out of range: "
        -9999   || IllegalArgumentException || "Port value out of range: "
    }

    def "Should fail given too high port number: #portNumber"() {
        when:
        server.start(portNum)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == String.format("%s%s", expectedMsg, portNum)

        where:
        portNum || expectedException        || expectedMsg
        65536   || IllegalArgumentException || "Port value out of range: "
        99999   || IllegalArgumentException || "Port value out of range: "
    }
}