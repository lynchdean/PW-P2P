package com.lynchd49.pwp2p.server

import spock.lang.Shared
import spock.lang.Specification

class SyncClientTest extends Specification {

    @Shared
    private int portNumber = 4444

    @Shared
    private SyncServer server

    private String hostName = "127.0.0.1"
    private SyncClient client

    def setupSpec() {
        server = new SyncServer(portNumber)
        server.start()
    }

    def cleanupSpec() {
        server.stop()
    }

    def setup() {
        client = new SyncClient()
        client.startConnection(hostName, portNumber)
    }

    def cleanup() {
        client.stopConnection()
    }

    def "isConnected() should return true if there is an active connections"() {
        expect:
        client.isConnected()
    }

//    def "Server should echo client message: #msg"() {
//        when:
//        String msg = client.sendMessage("echo")
//
//        then:
//        msg == "echo"
//    }
}
