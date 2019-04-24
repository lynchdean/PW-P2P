package com.lynchd49.pwp2p.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.Objects;

/* ClassFileServer.java -- a simple file server that can server
 * Http get request in both clear and secure channel
 *
 * The ClassFileServer implements a ClassServer that
 * reads files from the file system. See the
 * doc for the "Main" method for how to run this
 * server.
 */
public class ClassFileServer extends ClassServer {

    private final int serverPort;
    private final String docRoot;

    /**
     * Constructs a ClassFileServer.
     *
     * @param docRoot the path where the server locates files
     */
    private ClassFileServer(ServerSocket ss, int serverPort, String docRoot) {
        super(ss);
        this.serverPort = serverPort;
        this.docRoot = docRoot;
    }

    /**
     * Returns an array of bytes containing the bytes for
     * the file represented by the argument <b>path</b>.
     *
     * @return the bytes for the file
     * @exception FileNotFoundException if the file corresponding
     * to <b>path</b> could not be loaded.
     */
    public byte[] getBytes(String path)
            throws IOException
    {
        System.out.println("reading: " + path);
        File f = new File(docRoot + File.separator + path);
        int length = (int)(f.length());
        if (length == 0) {
            throw new IOException("File length is zero: " + path);
        } else {
            FileInputStream fin = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fin);

            byte[] byteCodes = new byte[length];
            in.readFully(byteCodes);
            return byteCodes;
        }
    }

    /**
     * Main method to create the class server that reads
     * files. This takes two command line arguments, the
     * port on which the server accepts requests and the
     * root of the path. To start up the server: <br><br>
     *
     * <code>   java ClassFileServer <port> <path>
     * </code><br><br>
     *
     * <code>   new ClassFileServer(port, docRoot);
     * </code>
     */
    public void main(String[] args)
    {
        System.out.println("USAGE: java ClassFileServer port docRoot [TLS [true]]");
        System.out.println();
        System.out.println("If the third argument is TLS, it will start as\n" +
                "a TLS/SSL file server, otherwise, it will be\n" +
                "an ordinary file server. \n" +
                "If the fourth argument is true,it will require\n" +
                "client authentication as well.");

        int port = serverPort;
        String docRoot = "";

        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        if (args.length >= 2) {
            docRoot = args[1];
        }
        String type = "PlainSocket";
        if (args.length >= 3) {
            type = args[2];
        }
        try {
            ServerSocketFactory ssf = ClassFileServer.getServerSocketFactory(type);
            ServerSocket ss = Objects.requireNonNull(ssf).createServerSocket(port);
            if (args.length >= 4 && args[3].equals("true")) {
                ((SSLServerSocket)ss).setNeedClientAuth(true);
            }
            new ClassFileServer(ss, serverPort, docRoot);
        } catch (IOException e) {
            System.out.println("Unable to start ClassServer: " +
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static ServerSocketFactory getServerSocketFactory(String type) {
        if (type.equals("TLS")) {
            SSLServerSocketFactory ssf;
            try {
                // set up key manager to do server authentication
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

                ssf = ctx.getServerSocketFactory();
                return ssf;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ServerSocketFactory.getDefault();
        }
        return null;
    }
}
