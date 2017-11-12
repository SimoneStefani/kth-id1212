package peer.net.server;

import protocol.PeersListMessage;
import protocol.UtilityMessage;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class StartupServerConnection {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public HashMap<String, PeerInfo> sendJoinMessage(PeerInfo currentPeerInfo) throws IOException, ClassNotFoundException {
        out.writeObject(new UtilityMessage("JOIN", currentPeerInfo));
        PeersListMessage peersListMessage = (PeersListMessage) in.readObject();
        HashMap<String, PeerInfo> peersTable = peersListMessage.getPeersTable();
        peersTable.remove(currentPeerInfo.getId());
        stopConnection();
        return peersTable;
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
