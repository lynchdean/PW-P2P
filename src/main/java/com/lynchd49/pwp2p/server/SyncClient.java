package com.lynchd49.pwp2p.server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncClient {
    private Socket socket;
    private File outputFile;
    private InputStream in;
    private OutputStream out;

    public void startConnection(String hostName, int portNumber) throws IOException {
        try {
            outputFile = new File("new_db.kdbx");
            socket = new Socket(hostName, portNumber);
            in = socket.getInputStream();
            out = new FileOutputStream("src/test/resources/output_test1.kdbx");
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about host %s%n", hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("Couldn't get I/O for the connection to %s%n", hostName);
            System.exit(1);
        }

        int count;
        byte[] buffer = new byte[8192];
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }

        System.out.println("Success!");
        stopConnection();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    //TODO remove once testing is successful
    public static void main(String[] args) {
        try {
            SyncClient client = new SyncClient();
            client.startConnection("localhost", 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
