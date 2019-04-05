package com.lynchd49.pwp2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncServer implements Runnable {

    private Thread serverThread;

    private final int portNumber;

    public SyncServer(int portNumber) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new IllegalArgumentException(String.format("Port value out of range: %d", portNumber));
        }
        this.portNumber = portNumber;
    }

    public void start() {
        serverThread = new Thread(this);
        serverThread.start();
    }

    public void stop() {
        serverThread = null;
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (serverThread == thisThread) {
            try {
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    out.println(inputLine);
                    Thread.sleep(1000);
                }

            } catch (IOException e) {
                System.out.printf("Exception caught when trying to listen on port %d or listening for a connection%n", portNumber);
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO remove once testing is successful
    public static void main(String[] args) {
        SyncServer server = new SyncServer(7898);
        server.start();
    }
}