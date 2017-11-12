package peer.controller;

import peer.net.client.PeerClient;
import peer.net.server.ControllerObserver;
import peer.net.server.PeerInfo;
import peer.net.server.PeerServer;
import peer.net.server.StartupServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Controller {
    private PeerInfo currentPeerInfo;
    private HashMap<String, PeerInfo> peersTable;


    public Controller() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            this.currentPeerInfo = new PeerInfo(serverSocket.getInetAddress().toString(), serverSocket.getLocalPort());
            new PeerServer().start(serverSocket, new ListManipulator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        Controller controller = new Controller();
        controller.joinNetwork();
        TimeUnit.SECONDS.sleep(5);
        controller.leaveNetwork();
    }

    public void joinNetwork() {
        System.out.println("Joining network...");
        CompletableFuture.runAsync(() -> {
            try {
                // Fetch list of peers from startup server
                // 1) Create StartupServerConnection
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                System.out.println("stt serv");
                // 2) Connect to startup server
                startupServerConnection.startConnection("127.0.0.1", 5555);
                System.out.println("cnnt");
                // 3) Fetch list of peers
                // 4) Return list of peers
                peersTable = startupServerConnection.sendJoinMessage(currentPeerInfo);
                System.out.println(peersTable);

                // Foreach peer in list send join message
                for (String peer : peersTable.keySet()) {
                    // 1) Create new PeerClient
                    PeerClient peerClient = new PeerClient();
                    // 2) Connect to peer
                    peerClient.startConnection("127.0.0.1", peersTable.get(peer).getPort());
                    // 3) Send join so that peer can add to list
                    peerClient.sendJoinMessage(currentPeerInfo);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Controller error!");
                e.printStackTrace();
            }
        });
    }

    public void sendMove(String move) {
        CompletableFuture.runAsync(() -> {
            try {
                // Foreach peer in list send move
                // 1) Create new PeerClient
                // 2) Connect to peer
                // 3) Send move
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public void leaveNetwork() {
        System.out.println("Leaving network...");
        CompletableFuture.runAsync(() -> {
            try {
                // Send leave message to startup server
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                startupServerConnection.startConnection("127.0.0.1", 5555);
                startupServerConnection.sendLeaveMessage(currentPeerInfo);

                // Foreach peer in list send leave message
                for (String peer : peersTable.keySet()) {
                    // 1) Create new PeerClient
                    PeerClient peerClient = new PeerClient();
                    // 2) Connect to peer
                    peerClient.startConnection("127.0.0.1", peersTable.get(peer).getPort());
                    // 3) Send leave so that peer can remove from list
                    peerClient.sendLeaveMessage(currentPeerInfo);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private class ListManipulator implements ControllerObserver {
        @Override
        public void addPeer(PeerInfo peer) {
            System.out.println("Adding peer: " + peer.getPort());
            peersTable.put(peer.getId(), peer);
        }

        @Override
        public void removePeer(PeerInfo peer) {
            System.out.println("Removing peer: " + peer.getPort());
            peersTable.remove(peer.getId());
        }
    }
}
