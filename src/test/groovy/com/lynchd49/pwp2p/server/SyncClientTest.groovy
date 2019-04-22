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
}
