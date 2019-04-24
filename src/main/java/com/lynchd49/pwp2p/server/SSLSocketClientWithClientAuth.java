package com.lynchd49.pwp2p.server;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyStore;

class SSLSocketClientWithClientAuth {
    public static void main(String[] args) {
        String host = null;
        int port = -1;
        String path = null;
        for (String arg : args) System.out.println(arg);

        if (args.length < 3) {
            System.out.println("USAGE: java SSLSocketClientWithClientAuth host port requestedFilePath");
            System.exit(-1);
        }

        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            path = args[2];
        } catch (IllegalArgumentException e) {
            System.out.println("USAGE: java SSLSocketClientWithClientAuth host port requestedFilePath");
            System.exit(-1);
        }

        try {
            /*
             * Set up a key manager for client authentication
             * if asked by the server.  Use the implementation's
             * default TrustStore and secureRandom routines.
             */
            SSLSocketFactory factory;
            try {
                SSLContext ctx;
                KeyManagerFactory kmf;
                KeyStore ks;
                char[] passphrase = "passphrase".toCharArray();

                ctx = SSLContext.getInstance("TLS");
                kmf = KeyManagerFactory.getInstance("SunX509");
                ks = KeyStore.getInstance("JKS");

                ks.load(new FileInputStream("testkeys"), passphrase);
                kmf.init(ks, passphrase);
                ctx.init(kmf.getKeyManagers(), null, null);

                factory = ctx.getSocketFactory();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }

            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);

            /*
             * send http request
             *
             * See SSLSocketClient.java for more information about why
             * there is a forced handshake here when using PrintWriters.
             */
            socket.startHandshake();

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            out.println(String.format("GET %s HTTP/1.0", path));
            out.println();
            out.flush();

            /*
             * Make sure there were no surprises
             */
            if (out.checkError())
                System.out.println("SSLSocketClient: java.io.PrintWriter error");

            /* read response */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
