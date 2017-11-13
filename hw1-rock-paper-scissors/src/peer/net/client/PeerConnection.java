package peer.net.client;

import common.Message;
import common.MessageWrapper;
import peer.net.server.PeerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PeerConnection {
    private Socket peerSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Open a connection with a specific peer in the
     * network and start streams.
     *
     * @param ip of the peer
     * @param port of the peer
     * @throws IOException
     */
    public void startConnection(String ip, int port) throws IOException {
        peerSocket = new Socket(ip, port);
        out = new ObjectOutputStream(peerSocket.getOutputStream());
        in = new ObjectInputStream(peerSocket.getInputStream());
    }

    /**
     * Send a JOIN message to a peer in order to be added
     * in its peersTable. The peer will respond with the
     * current info that include the currentMove if it has
     * been already made.
     *
     * @param currentPeerInfo info of the requesting peer
     * @return the updated info of the responding peer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PeerInfo sendJoinMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new MessageWrapper(Message.JOIN, currentPeerInfo));
        return (PeerInfo) in.readObject();
    }

    /**
     * Send a LEAVE message to a peer in order to be
     * removed from its peersTable.
     *
     * @param currentPeerInfo info of the requesting peer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendLeaveMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new MessageWrapper(Message.LEAVE, currentPeerInfo));
    }

    /**
     * Notify a peer that a MOVE has been selected.
     *
     * @param move type (ROCK, PAPER, SCISSORS)
     * @param currentPeerInfo info of the requesting peer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendMoveMessage(String move, PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new MessageWrapper(Message.MOVE, currentPeerInfo, move));
    }

    /**
     * Close streams and socket with peer.
     *
     * @throws IOException
     */
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        peerSocket.close();
    }
}
