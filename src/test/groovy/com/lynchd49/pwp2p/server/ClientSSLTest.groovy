package com.lynchd49.pwp2p.server

import spock.lang.Specification

class ClientSSLTest extends Specification {

    ClientSSL client

    def setup() {
        client = new ClientSSL()
    }

    def "IsTransferSuccess is false before transfer"() {
        expect:
        !client.isTransferSuccess()
    }

    def "IsConnectionSuccess is false before connection"() {
        expect:
        !client.isConnectionSuccess()
    }
}
