package com.lynchd49.syncsafe.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
public void start(int portNumber) throws IOException {
    try (
            ServerSocket serverSocket =
                    new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
    ) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            out.println(inputLine);
        }
    } catch (IOException e) {
        System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
        System.out.println(e.getMessage());
    }
}

    public static void main(String[] args) throws IOException {
        EchoServer server = new EchoServer();
        server.start(4445);
    }
}