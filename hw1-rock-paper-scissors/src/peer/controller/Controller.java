package peer.controller;

import peer.net.server.PeerInfo;
import peer.net.server.PeerServer;
import peer.net.server.StartupServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private PeerInfo currentPeerInfo;
    private HashMap<String, PeerInfo> peersTable;


    public Controller() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            this.currentPeerInfo = new PeerInfo(serverSocket.getInetAddress().toString(), serverSocket.getLocalPort());
            new PeerServer().start(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Controller controller = new Controller();
        controller.joinNetwork();
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
                // 1) Create new PeerClient
                // 2) Connect to peer
                // 3) Send join so that peer can add to list
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Controller error!");
                e.printStackTrace();
            }
        });
    }

//    public void sendMove(String move) {
//        CompletableFuture.runAsync(() -> {
//            try {
//                // Foreach peer in list send move
//                // 1) Create new PeerClient
//                // 2) Connect to peer
//                // 3) Send move
//            } catch (IOException e) {
//                throw new UncheckedIOException(e);
//            }
//        });
//    }
//
//    public void leaveNetwork() {
//        CompletableFuture.runAsync(() -> {
//            try {
//                // Send leave message to startup server
//
//                // Foreach peer in list send leave message
//                // 1) Create new PeerClient
//                // 2) Connect to peer
//                // 3) Send join so that peer can remove from list
//            } catch (IOException e) {
//                throw new UncheckedIOException(e);
//            }
//        });
//    }
}
