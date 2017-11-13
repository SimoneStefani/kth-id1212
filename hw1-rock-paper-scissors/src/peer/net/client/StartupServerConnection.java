package peer.net.client;

import common.Message;
import common.MessageWrapper;
import common.PeersTable;
import peer.net.server.PeerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class StartupServerConnection {
    private Socket peerSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Open a connection with the startup server.
     *
     * @param ip of the server
     * @param port of the server
     * @throws IOException
     */
    public void startConnection(String ip, int port) throws IOException {
        peerSocket = new Socket(ip, port);
        out = new ObjectOutputStream(peerSocket.getOutputStream());
        in = new ObjectInputStream(peerSocket.getInputStream());
    }

    /**
     * Send a JOIN message to the startup server in order
     * to be added in its peersTable. The server will
     * respond with a table of the peers currently
     * connected to the P2P network.
     *
     * @param currentPeerInfo info of the requesting peer
     * @return the peersTable
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PeersTable sendJoinMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new MessageWrapper(Message.JOIN, currentPeerInfo));
        PeersTable peersTable = (PeersTable) in.readObject();
        peersTable.removePeerFromTable(currentPeerInfo.getId());
        return peersTable;
    }

    /**
     * Send a LEAVE message to the startup server in order
     * to be removed from its peersTable.
     *
     * @param currentPeerInfo info of the requesting peer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendLeaveMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new MessageWrapper(Message.LEAVE, currentPeerInfo));
    }

    /**
     * Close streams and socket with startup server.
     *
     * @throws IOException
     */
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        peerSocket.close();
    }
}
