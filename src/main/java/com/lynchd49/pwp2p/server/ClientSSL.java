package com.lynchd49.pwp2p.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.security.KeyStore;

public class ClientSSL {

    private static final Logger LOGGER = LogManager.getRootLogger();
    private volatile boolean running;

    private boolean transferSuccess = false;
    private boolean connectionSuccess = false;

    public void start(String host, int port, String requestedFile) {
//        running = true;
//        while (running) {
            LOGGER.info("Client started.");
            try {
                SSLSocketFactory factory;
                try {
                    SSLContext ctx;
                    KeyManagerFactory kmf;
                    KeyStore ks;
                    char[] passphrase = "passphrase".toCharArray();

                    ctx = SSLContext.getInstance("TLS");
                    kmf = KeyManagerFactory.getInstance("SunX509");
                    ks = KeyStore.getInstance("JKS");

                    ks.load(new FileInputStream("src/main/resources/clientkeys/testkeys"), passphrase);
                    kmf.init(ks, passphrase);
                    ctx.init(kmf.getKeyManagers(), null, null);

                    factory = ctx.getSocketFactory();
                } catch (Exception e) {
                    throw new IOException(e.getMessage());
                }

                SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
                socket.startHandshake();

                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.println(String.format("GET %s HTTP/1.0", requestedFile));
                out.println();
                out.flush();

                if (out.checkError())
                    LOGGER.error("SSLSocketClient: java.io.PrintWriter error");

                LOGGER.info("Connected to host successfully.");
                connectionSuccess = true;
                InputStream in = socket.getInputStream();
                OutputStream fileOut = new FileOutputStream(String.format("./%s", requestedFile));

                int byteRead;
                // Eat header
                boolean eatHeader = true;
                while (eatHeader) {
                    byteRead = in.read();
                    if ((char) byteRead == '\r') {
                        in.read();
                        byteRead = in.read();
                        if (byteRead == '\r') {
                            in.read();
                            eatHeader = false;
                        }
                    }
                }

                // Write file
                // TODO change this to save in the same directory as current database.
                byte[] buffer = new byte[8192];
                while ((byteRead = in.read(buffer)) > 0) {
                    fileOut.write(buffer, 0, byteRead);
                }
                LOGGER.info("File received successfully.");
                transferSuccess = true;

                in.close();
                out.close();
                fileOut.close();
                socket.close();
            } catch (UnknownHostException e) {
                LOGGER.error(String.format("Unknown host: %s", host));
            } catch (FileNotFoundException e) {
                LOGGER.error(String.format("Requested file not found: %s", requestedFile));
            } catch (IOException e) {
                LOGGER.error(String.format("Couldn't get I/O for the connection to %s", host));
            }
        LOGGER.info("Client stopped.");
    }

    public boolean isTransferSuccess() {
        return transferSuccess;
    }

    public boolean isConnectionSuccess() {
        return connectionSuccess;
    }

    // TODO remove this when implemented into GUI
    public static void main(String[] args) {
        ClientSSL client = new ClientSSL();
        client.start("localhost", 1235, "/test2.kdbx");
    }
}
