package com.lynchd49.pwp2p.server;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ServerSSL implements Runnable {

    private static final Logger LOGGER = LogManager.getRootLogger();

    private volatile boolean running;
    private ServerSocket serverSocket;

    private final String expectedClientAddr;
    private final String currentDbName;

    private boolean transferSuccess = false;
    private boolean isMismatch = false;

    ServerSSL(ServerSocket ss, String expectedClientAddr, String currentDbName) {
        this.serverSocket = ss;
        this.expectedClientAddr = expectedClientAddr;
        this.currentDbName = currentDbName;
    }

    public abstract byte[] getBytes(String path) throws IOException;

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
                LOGGER.warn(String.format("Server stopped, but failed to close port"));
            }
        }
        LOGGER.info("Server stopped.");
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            Socket socket;

            // Accept connection from client
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Class Server died: " + e.getMessage());
                e.printStackTrace();
                return;
            }


            String socketAddress = socket.getInetAddress().getHostAddress();
            String socketHostname = socket.getInetAddress().getHostName();
            if (socketAddress.equals(expectedClientAddr) || socketHostname.equals(expectedClientAddr)
                    || expectedClientAddr.equals(("localhost")) || expectedClientAddr.equals("127.0.0.1")) {
                try {
                    OutputStream rawOut = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(rawOut)));
                    try {
                        // Get path to class file from header
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String path = getPath(in);

                        // Retrieve bytecodes
                        byte[] bytecodes = getBytes(path);

                        // Send bytecodes in response
                        try {
                            out.print("HTTP/1.0 200 OK\r\n");
                            out.print("Content-Length: " + bytecodes.length +
                                    "\r\n");
                            out.print("Content-Type: text/html\r\n\r\n");
                            out.flush();
                            rawOut.write(bytecodes);
                            rawOut.flush();
                            transferSuccess = true;
                            LOGGER.info(String.format("File %s sent successfully.", path));
                        } catch (IOException ie) {
                            ie.printStackTrace();
                            return;
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error transferring file", e);
                        out.println(String.format("HTTP/1.0 400 %s\r\n", e.getMessage()));
                        out.println("Content-Type: text/html\r\n\r\n");
                        out.flush();
                    }

                } catch (IOException ex) {
                    LOGGER.error(String.format("error writing response: %s", ex.getMessage()));
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        LOGGER.error("Error trying to close server socket.", e);
                    }
                }
                this.stop();
            } else {
                try {
                    socket.close();
                    LOGGER.error("Client address doesn't match");
                    isMismatch = true;
                    this.stop();
                } catch (IOException e) {
                    LOGGER.error("Error trying to close server socket.", e);
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isSuccessful() {
        return transferSuccess;
    }

    public boolean isMismatch() {
        return isMismatch;
    }

    private static String getPath(BufferedReader in) throws IOException {
        String line = in.readLine();
        String path = "";

        // Extract class
        if (line.startsWith("GET /")) {
            line = line.substring(5, line.length() - 1).trim();
            int index = line.indexOf(' ');
            if (index != -1) {
                path = line.substring(0, index);
            }
        }

        // Eat the rest of header
        do {
            line = in.readLine();
        } while ((line.length() != 0) && (line.charAt(0) != '\r') && (line.charAt(0) != '\n'));

        if (path.length() != 0) {
            return path;
        } else {
            throw new IOException("Malformed Header");
        }
    }
}
