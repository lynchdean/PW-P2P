package com.lynchd49.pwp2p.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncServer implements Runnable {

    private Thread serverThread;
    private final int portNumber;
    private final String filePath;

    public SyncServer(int portNumber, String filePath) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new IllegalArgumentException(String.format("Port value out of range: %d", portNumber));
        }
        this.portNumber = portNumber;
        this.filePath = filePath;
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
                File file = new File(filePath);
                InputStream in = new FileInputStream(file);
                OutputStream out = clientSocket.getOutputStream();

                int count;
                byte[] buffer = new byte[8192];
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }

                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close();
            } catch (FileNotFoundException e) {
                System.out.printf("File not found: %s ", filePath);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.printf("Exception caught when trying to listen on port %d or listening for a connection", portNumber);
                e.printStackTrace();
            }
            stop();
        }
    }

    //TODO remove once testing is successful
    public static void main(String[] args) {
        SyncServer server = new SyncServer(4444, "/home/dean/Projects/2019-ca400-lynchd49/src/test/resources/test1.kdbx");
        server.start();
    }
}