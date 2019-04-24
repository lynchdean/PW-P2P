package com.lynchd49.pwp2p.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * ClassServer.java -- a simple file server that can serve
 * Http get request in both clear and secure channel
 */

public abstract class ClassServer implements Runnable {

    private ServerSocket server;
    /**
     * Constructs a ClassServer based on <b>serverSocket</b> and
     * obtains a file's bytecodes using the method <b>getBytes</b>.
     *
     */
    ClassServer(ServerSocket serverSocket) {
        server = serverSocket;
        newListener();
    }

    /**
     * Returns an array of bytes containing the bytes for
     * the file represented by the argument <b>path</b>.
     *
     * @return the bytes for the file
     * @exception FileNotFoundException if the file corresponding
     * to <b>path</b> could not be loaded.
     * @exception IOException if error occurs reading the class
     */
    public abstract byte[] getBytes(String path) throws IOException;

    /**
     * The "listen" thread that accepts a connection to the
     * server, parses the header to obtain the file name
     * and sends back the bytes for the file (or error
     * if the file is not found or the response was malformed).
     */
    public void run() {
        Socket socket;

        // accept a connection
        try {
            socket = server.accept();
        } catch (IOException e) {
            System.out.println("Class Server died: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // create a new thread to accept the next connection
        newListener();
        try {
            OutputStream rawOut = socket.getOutputStream();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(rawOut)));

            try {
                // get path to class file from byteCodes header
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String path = getPath(in);
                // retrieve byteCodes
                byte[] byteCodes = getBytes(path);
                // send byteCodes in response (assumes HTTP/1.0 or later)
                try {
                    out.print("HTTP/1.0 200 OK\r\n");
                    out.print(String.format("Content-Length: %d\r\n", byteCodes.length));
                    out.print("Content-Type: text/html\r\n\r\n");
                    out.flush();
                    rawOut.write(byteCodes);
                    rawOut.flush();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // write out error response
                out.println(String.format("HTTP/1.0 400 %s\r\n", e.getMessage()));
                out.println("Content-Type: text/html\r\n\r\n");
                out.flush();
            }

        } catch (IOException ex) {
            // eat exception (could log error to log file, but
            // write out to stdout for now).
            System.out.printf("error writing response: %s%n", ex.getMessage());
            ex.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a new thread to listen.
     */
    private void newListener() {
        new Thread(this).start();
    }

    /**
     * Returns the path to the file obtained from
     * parsing the HTML header.
     */
    private static String getPath(BufferedReader in) throws IOException {
        String line = in.readLine();
        String path = "";

        // extract class from GET line
        if (line.startsWith("GET /")) {
            line = line.substring(5, line.length()-1).trim();
            int index = line.indexOf(' ');
            if (index != -1) {
                path = line.substring(0, index);
            }
        }

        // eat the rest of header
        line = in.readLine();
        while ((line.length() != 0) && (line.charAt(0) != '\r') && (line.charAt(0) != '\n')) {
            line = in.readLine();
        }

        if (path.length() != 0) {
            return path;
        } else {
            throw new IOException("Malformed Header");
        }
    }
}