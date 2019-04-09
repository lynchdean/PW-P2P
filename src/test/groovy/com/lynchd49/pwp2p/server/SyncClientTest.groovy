package com.lynchd49.pwp2p.server

import spock.lang.Shared
import spock.lang.Specification

class SyncClientTest extends Specification {

    @Shared
    private int portNumber = 4444

    @Shared
    private String testFilePath = "test1.kdbx"

    @Shared
    private SyncServer server

    private String hostName = "127.0.0.1"
    private SyncClient client

    def setupSpec() {
        server = new SyncServer(portNumber, testFilePath)
        server.start()
    }

    def cleanupSpec() {
        server.stop()
    }

    def setup() {
        String outputFilePath = "src/test/resources/output_test1.kdbx"
        client = new SyncClient()
        client.sendPWDB(hostName, portNumber, outputFilePath)
    }

    def cleanup() {
        client.stopConnection()
    }

//    def "Server should echo client message: #msg"() {
//        when:
//        String msg = client.sendMessage("echo")
//
//        then:
//        msg == "echo"
//    }
}
