package com.lynchd49.pwp2p.server

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class FileServerSSLTest extends Specification {

    def "Get SSL server socket given port"() {
        when:
        ServerSocket ss = FileServerSSL.getServerSocket(4446)

        then:
        ss.localPort == 4446

        cleanup:
        ss.close()
    }

    def "Get IllegalArgumentException when getServerSocket() port is out of range: #port"() {
        when:
        FileServerSSL.getServerSocket(port)

        then:
        thrown(IllegalArgumentException)

        where:
        port  | _
        65536 | _
        99999 | _
    }
}
