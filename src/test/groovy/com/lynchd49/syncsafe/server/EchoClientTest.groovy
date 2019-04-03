package com.lynchd49.syncsafe.server

import spock.lang.Specification

class EchoClientTest extends Specification {
    def "Server should echo message from Client"() {
        when:
        EchoServer server = new EchoServer()
        server.start(4444)
        EchoClient client = new EchoClient()
        client.startConnection("localhost", 4444)

        then:
        client.sendMessage("echo") == "echo"
    }
}
