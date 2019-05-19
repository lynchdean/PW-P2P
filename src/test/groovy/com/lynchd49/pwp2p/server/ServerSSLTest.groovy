package com.lynchd49.pwp2p.server

import spock.lang.Specification

class ServerSSLTest extends Specification {

    ServerSSL server

    def setup() {
        ServerSocket ss = FileServerSSL.getServerSocket(4444)
        server = new ServerSSL(ss, "localhost", "test1.kdbx") {
            @Override
            byte[] getBytes(String path) throws IOException {
                return new byte[0]
            }
        }
    }

    def "Does the server stop correctly once stop() has been clalled"() {
        when:
        server.start()
        boolean isRunningInitially = server.isRunning()
        server.stop()

        then:
        isRunningInitially
        !server.isRunning()
    }

    def "Is the server running correctly when start is called"() {
        when:
        boolean isRunningInitially = server.isRunning()
        server.start()

        then:
        !isRunningInitially
        server.isRunning()

        cleanup:
        server.stop()
    }

    def "IsRunning returns the correct value at each stage"() {
        when:
        // Before
        boolean isRunningInitially = server.isRunning()
        server.start()
        // During
        boolean isRunningDuring = server.isRunning()
        server.stop()

        then:
        !isRunningInitially
        isRunningDuring
        // After
        !server.isRunning()
    }

    def "IsSuccessful returns false before transfer is made"() {
        expect:
        !server.isSuccessful()
    }

    def "IsMismatch returns false before connection is made"() {
        expect:
        !server.isMismatch()
    }
}
