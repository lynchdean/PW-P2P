package com.lynchd49.pwp2p.server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncClient {
    public void sendPWDB(String hostName, int portNumber, String outputFilePath) {
        try {
            Socket socket = new Socket(hostName, portNumber);
            InputStream in = socket.getInputStream();
            OutputStream out = new FileOutputStream(outputFilePath);

            int count;
            byte[] buffer = new byte[8192];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            in.close();
            out.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.printf("Unknown host: %s%n", hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("Couldn't get I/O for the connection to %s%n", hostName);
            System.exit(1);
        }
    }

    //TODO remove once testing is successful
    public static void main(String[] args) {
        SyncClient client = new SyncClient();
        client.sendPWDB("localhost", 4444,"src/test/resources/output_test1.kdbx"
        );
    }
}
