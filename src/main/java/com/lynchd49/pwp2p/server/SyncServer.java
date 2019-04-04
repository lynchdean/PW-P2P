package com.lynchd49.pwp2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.printf("Exception caught when trying to listen on port %d or listening for a connection%n", portNumber);
            System.out.println(e.getMessage());
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }


    //TODO remove once testing is successful
    public static void main(String[] args) {
        SyncServer server = new SyncServer();
        server.start(4444);
    }
}