package com.lynchd49.syncsafe.server

import spock.lang.Specification

class SyncClientTest extends Specification {

    private int port = 6666
    private SyncServer server

    def setup() {
        server = new SyncServer()
        server.start(port)
    }

    def cleanup() {
        server.stop()
    }

    def "Should receive correct response from server"() {
        when:
        SyncClient client = new SyncClient()
        client.startConnection("127.0.0.1", port)
        String response = client.sendMessage("hello server")

        then:
        response == "hello client"
    }
}
