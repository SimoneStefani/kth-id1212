package startupserver;

import common.PeersTable;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartupServer {
    private static final Logger LOGGER = Logger.getLogger(ServerSocket.class.getName());
    PeersTable peersTable = new PeersTable();
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        LOGGER.info("Starting startup server");
        StartupServer server = new StartupServer();
        server.start(8080);
    }

    /**
     * Start the StartupServer and wait for incoming
     * connection request. When a new connection is
     * received delegate it to a new StartupPeerHandler.
     *
     * @param port where the StartupServer is listening
     */
    private void start(int port) {
        try {
            LOGGER.info("Waiting for connection from peer");
            serverSocket = new ServerSocket(port);
            while (true) new PeerHandler(serverSocket.accept(), this).run();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * Stop the StartupServer and close the socket.
     */
    public void stop() {
        try {
            LOGGER.info("Stopping startup server");
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
