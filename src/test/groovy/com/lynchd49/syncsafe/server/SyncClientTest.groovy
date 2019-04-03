package com.lynchd49.syncsafe.server

import spock.lang.Specification

class SyncClientTest extends Specification {

    private String hostName = "localhost"
    private int portNumber = 4444

    private SyncServer server

    def setup() {
        server = new EchoServer();
        server.start(portNumber)

        mockClient = Mock(SyncClient.class)
    }

    def "isConnected() should return false if there are no active connections"() {
        expect:
        !mockClient.isConnected()
    }

    def "Server should echo client message: #msg"() {
        when:
        SyncClient client = new SyncClient()
        client.startConnection(hostName, portNumber)
        String msg = client.sendMessage("echo")

        then:
        msg == "echo"
    }
}
