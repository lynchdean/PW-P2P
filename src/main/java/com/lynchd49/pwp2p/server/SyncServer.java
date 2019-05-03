package com.lynchd49.pwp2p.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SyncServer implements Runnable {


    private static final Logger LOGGER = LogManager.getRootLogger();

    private final int portNumber;
    private final String recipientHostname;
    private final String filePath;

    private ServerSocket serverSocket;
    private volatile boolean running;

    private boolean transferSuccess = false;

    public SyncServer(int portNumber, String recipientHostname, String filePath) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new IllegalArgumentException(String.format("Port value out of range: %d", portNumber));
        }
        this.portNumber = portNumber;
        this.recipientHostname = recipientHostname;
        this.filePath = filePath;
    }

    public void start() {
        LOGGER.info("Starting server...");
        Thread waiter = new Thread(this);
        waiter.start();
        LOGGER.info("Server running.");
    }

    public void stop() {
        LOGGER.info("Stopping server...");
        this.running = false;
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.warn(String.format("Server stopped, but failed to close port: %d", portNumber));
            }
        }
        LOGGER.info("Server stopped.");
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            try {
                serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
//                if (serverSocket.getInetAddress().getHostAddress().equals(recipientHostname) ||
//                        recipientHostname.equals("localhost") || recipientHostname.equals("127.0.0.1")) {
                    File file = new File(filePath);
                    InputStream in = new FileInputStream(file);
                    OutputStream out = clientSocket.getOutputStream();

                    int count;
                    byte[] buffer = new byte[8192];
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                    }

                    LOGGER.info("File transferred successfully.");
                    in.close();
                    out.close();
                    clientSocket.close();
                    transferSuccess = true;
//                }
            } catch (SocketException e) {
                LOGGER.warn("Socket has been closed from an external method (i.e. 'Stop Connection' button in GUI).");
            } catch (FileNotFoundException e) {
                LOGGER.error(String.format("File not found: %s", filePath));
                e.printStackTrace();
            } catch (IOException e) {
                LOGGER.error(String.format("Exception caught when trying to listen on port %s or listening for a connection", portNumber));
                e.printStackTrace();
            }
            this.stop();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isSuccessful() {
        return transferSuccess;
    }
}