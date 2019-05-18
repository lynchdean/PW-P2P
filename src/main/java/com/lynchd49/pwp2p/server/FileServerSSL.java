package com.lynchd49.pwp2p.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.Objects;

public class FileServerSSL extends ServerSSL {

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static String docRoot;

    public FileServerSSL(int port, String docRoot, String clientAddress, String currentDbName) throws IOException {
        super(getServerSocket(port), clientAddress, currentDbName);
        FileServerSSL.docRoot = docRoot;
    }

    public byte[] getBytes(String path) throws IOException {
        LOGGER.info("reading: " + path);
        File f = new File(docRoot + File.separator + path);
        int length = (int) (f.length());
        if (length == 0) {
            LOGGER.error("Incorrect file requested.");
            throw new IOException("File length is zero: " + path);
        } else {
            FileInputStream fin = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fin);

            byte[] bytecodes = new byte[length];
            in.readFully(bytecodes);
            return bytecodes;
        }
    }

    public static ServerSocket getServerSocket(int port) throws IOException {
        ServerSocketFactory ssf = FileServerSSL.getServerSocketFactory();
        ServerSocket ss = Objects.requireNonNull(ssf).createServerSocket(port);
        ((SSLServerSocket) ss).setNeedClientAuth(true);
        return ss;
    }

    private static ServerSocketFactory getServerSocketFactory() {
        SSLServerSocketFactory ssf;
        try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "passphrase".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("src/main/resources/serverkeys/testkeys"), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            LOGGER.error("Error getting serverSocketFactory");
            e.printStackTrace();
        }
        return null;
    }

    // Main method for manual testing purposes
//    public static void main(String[] args) throws IOException {
//        int port = 1235;
//        ServerSocket ss = FileServerSSL.getServerSocket(port);
//        FileServerSSL server = new FileServerSSL(ss, "src/test/resources/");
//        server.start();
//    }
}