package startupserver;

import common.Message;
import common.MessageWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(PeerHandler.class.getName());
    private Socket clientSocket;
    private StartupServer server;

    PeerHandler(Socket socket, StartupServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    /**
     * Handle a request from a peer.
     */
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Read message
            MessageWrapper message = (MessageWrapper) in.readObject();

            // If JOIN, add peer to table and return table in PeersTableWrapper
            // If LEAVE, remove peer from table
            if (message.getMessage().equals(Message.JOIN)) {
                LOGGER.info("Peer at " + message.getSenderPeerInfo().getPort() + " is joining the network");
                server.peersTable.addPeerToTable(message.getSenderPeerInfo());
                out.writeObject(server.peersTable);
                System.out.println(server.peersTable);
            } else if (message.getMessage().equals(Message.LEAVE)) {
                LOGGER.info("Peer at " + message.getSenderPeerInfo().getPort() + " is leaving the network");
                server.peersTable.removePeerFromTable(message.getSenderPeerInfo().getId());
                System.out.println(server.peersTable);
            }

            // Close streams and socket
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
