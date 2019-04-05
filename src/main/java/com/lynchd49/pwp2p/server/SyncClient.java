package com.lynchd49.pwp2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String hostName, int portNumber) {
        try {
            clientSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about host %s%n", hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("Couldn't get I/O for the connection to %s%n", hostName);
            System.exit(1);
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    //TODO remove once testing is successful
    public static void main(String[] args) {
        try {
            SyncClient client = new SyncClient();
            client.startConnection("localhost", 7898);
            client.sendMessage("echo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
