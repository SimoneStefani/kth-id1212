package peer.net.client;

import peer.net.server.PeerInfo;
import protocol.UtilityMessage;

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

    public void sendJoinMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("JOIN", currentPeerInfo));
    }

    public void sendLeaveMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("LEAVE", currentPeerInfo));
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
