package com.lynchd49.syncsafe.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {
    private Socket echoSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void startConnection(String hostName, int portNumber) throws IOException {
        try {
            echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about host %s%n", hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("Couldn't get I/O for the connection to %s%n", hostName);
            System.exit(1);
        }
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }
}