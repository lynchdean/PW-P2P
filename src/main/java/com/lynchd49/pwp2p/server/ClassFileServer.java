package com.lynchd49.pwp2p.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;

/* ClassFileServer.java -- a simple file server that can server
 * Http get request in both clear and secure channel
 *
 * The ClassFileServer implements a ClassServer that
 * reads files from the file system. See the
 * doc for the "Main" method for how to run this
 * server.
 */

public class ClassFileServer extends ClassServer {

    private static int serverPort;
    private static String docRoot;
    private static boolean authenticate;

    /**
     * Constructs a ClassFileServer.
     *
     * @param serverPort the port the server will be run on
     * @param docRoot the path where the server locates files
     */
    public ClassFileServer(ServerSocket ss, int serverPort, String docRoot, boolean authenticate) {
        super(ss);
        this.serverPort = serverPort;
        this.docRoot = docRoot;
        this.authenticate = authenticate;
    }

    /**
     * Returns an array of bytes containing the bytes for
     * the file represented by the argument <b>path</b>.
     *
     * @return the bytes for the file
     * @throws FileNotFoundException if the file corresponding
     *                               to <b>path</b> could not be loaded.
     */
    public byte[] getBytes(String path)
            throws IOException {
        System.out.println("reading: " + path);
        File f = new File(docRoot + File.separator + path);
        int length = (int) (f.length());
        if (length == 0) {
            throw new IOException("File length is zero: " + path);
        } else {
            FileInputStream fin = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fin);

            byte[] bytecodes = new byte[length];
            in.readFully(bytecodes);
            return bytecodes;
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
    public static void start() {
        System.out.println(
                "USAGE: java ClassFileServer port docRoot [TLS [true]]\n\n" +
                "If the third argument is TLS, it will start as\n" +
                "a TLS/SSL file server, otherwise, it will be\n" +
                "an ordinary file server. \n" +
                "If the fourth argument is true,it will require\n" +
                "client authentication as well.");

        String docPath = docRoot;
        int port = serverPort;
        String type = "TLS";

        try {
            ServerSocketFactory ssf = ClassFileServer.getServerSocketFactory(type);
            ServerSocket ss = ssf.createServerSocket(port);
            if (authenticate) {
                ((SSLServerSocket) ss).setNeedClientAuth(true);
            }
            new ClassFileServer(ss, port, docPath, authenticate);
        } catch (IOException e) {
            System.out.printf("Unable to start ClassServer: %s%n", e.getMessage());
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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4444);
        ClassFileServer server = new ClassFileServer(serverSocket, 4444, "test2.kdbx", true);
        server.start();
    }
}