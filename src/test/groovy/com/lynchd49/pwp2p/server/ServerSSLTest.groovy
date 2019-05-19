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

    def "IsMismatch & IsSuccessfulreturns false before connection is made"() {
        expect:
        !server.isMismatch()
        !server.isSuccessful()

    }

}
