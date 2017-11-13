package peer.net.client;

import peer.net.server.PeerInfo;
import common.UtilityMessage;

import java.io.*;
import java.net.Socket;

public class PeerClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public PeerInfo sendJoinMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("JOIN", currentPeerInfo));
        return (PeerInfo) in.readObject();
    }

    public void sendLeaveMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("LEAVE", currentPeerInfo));
    }

    public void sendMoveMessage(String move, PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("MOVE", currentPeerInfo, move));
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
