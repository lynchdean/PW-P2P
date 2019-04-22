package com.lynchd49.pwp2p.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncClient {

    private static final Logger LOGGER = LogManager.getRootLogger();

    public void start(String hostname, int portNumber, String outputFilePath) {
        LOGGER.info("Starting client");
        try {
            Socket socket = new Socket(hostname, portNumber);
            InputStream in = socket.getInputStream();
            OutputStream out = new FileOutputStream(outputFilePath.replace(".kdbx", "_new.kdbx"));

            LOGGER.info("Client started.");
            int count;
            byte[] buffer = new byte[8192];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            in.close();
            out.close();
            socket.close();
            LOGGER.info("Client stopped.");
        } catch (UnknownHostException e) {
            LOGGER.error(String.format("Unknown host: %s", hostname));
        } catch (IOException e) {
            LOGGER.error(String.format("Couldn't get I/O for the connection to %s", hostname));
        }
    }
}
