package startupserver;

import peer.net.server.PeerInfo;
import protocol.PeersListMessage;
import protocol.UtilityMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class StartupServer {
    private ServerSocket serverSocket;
    private HashMap<String, PeerInfo> peersTable = new HashMap<>();

    public static void main(String[] args) throws IOException {
        StartupServer server = new StartupServer();
        server.start(5555);
    }

    public void start(int port) throws IOException {
        System.out.println("Starting server...");
        serverSocket = new ServerSocket(port);
        while (true) new StartupPeerHandler(serverSocket.accept(), this).run();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public HashMap<String, PeerInfo> addPeerToTable(String id, PeerInfo peerInfo) {
        this.peersTable.put(id, peerInfo);
        return peersTable;
    }

    private static class StartupPeerHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private StartupServer server;

        public StartupPeerHandler(Socket socket, StartupServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            System.out.println("New startup connection");
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                UtilityMessage message = (UtilityMessage) in.readObject();
                System.out.println("New startup message: " + message.getMessage());
                // If message JOIN, add to list and return list
                if (message.getMessage().equals("JOIN")) {
                    System.out.println("JOIN: " + message.getSenderPeerInfo());
                    HashMap<String, PeerInfo> peersTable = server.addPeerToTable(
                            message.getSenderPeerInfo().getId(),
                            message.getSenderPeerInfo());
                    out.writeObject(new PeersListMessage(peersTable));
                }

                // If message LEAVE, remove from list

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
