package peer.controller;

import peer.net.client.PeerConnection;
import peer.net.client.StartupServerConnection;
import peer.net.server.*;

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
            new PeerServer().start(serverSocket, new ListManipulator());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    // 1) Create new PeerConnection
                    PeerConnection peerConnection = new PeerConnection();
                    // 2) Connect to peer
                    peerConnection.startConnection("127.0.0.1", peersTable.get(peer).getPort());
                    // 3) Send join so that peer can add to list
                    PeerInfo syncedPeerInfo = peerConnection.sendJoinMessage(currentPeerInfo);
                    peersTable.replace(peer, syncedPeerInfo);

                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Controller error!");
                e.printStackTrace();
            }
        });
    }

    public void sendMove(String move, OutputHandler console) {
        // Check for already move
        currentPeerInfo.setCurrentMove(move);
        CompletableFuture.runAsync(() -> {
            try {
                // Foreach peer in list send move
                for (String peer : peersTable.keySet()) {
                    // 1) Create new PeerConnection
                    PeerConnection peerConnection = new PeerConnection();
                    // 2) Connect to peer
                    peerConnection.startConnection("127.0.0.1", peersTable.get(peer).getPort());
                    // 3) Send move
                    peerConnection.sendMoveMessage(move, currentPeerInfo);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).thenRun(() -> console.handleMsg(checkEndGame()));
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
                    // 1) Create new PeerConnection
                    PeerConnection peerConnection = new PeerConnection();
                    // 2) Connect to peer
                    peerConnection.startConnection("127.0.0.1", peersTable.get(peer).getPort());
                    // 3) Send leave so that peer can remove from list
                    peerConnection.sendLeaveMessage(currentPeerInfo);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private String checkEndGame() {
        String printout = "";
        boolean allScore = true;
        for (String peer : peersTable.keySet()) {
            if (peersTable.get(peer).getCurrentMove() == null) allScore = false;
        }
        if (allScore && currentPeerInfo.getCurrentMove() != null) {
            calculateScore(peersTable, currentPeerInfo);
            printout = printScore(currentPeerInfo);
            resetPeersMoves(peersTable);
            resetCurrentPeer(currentPeerInfo);
        }

        return  printout;
    }

    private void calculateScore(HashMap<String, PeerInfo> peersTable, PeerInfo currentPeerInfo) {
        for (String idA : peersTable.keySet()) {
            String moveA = peersTable.get(idA).getCurrentMove();

            for (String idB : peersTable.keySet()) {
                if (!idA.equals(idB)) {
                    String moveB = peersTable.get(idB).getCurrentMove();

                    if(moveA.equals("PAPER")) {
                        peersTable.get(idA).setRoundScore(moveB.equals("ROCK") ? 1 : 0);
                    }
                    if(moveA.equals("ROCK")) {
                        peersTable.get(idA).setRoundScore(moveB.equals("SCISSORS") ? 1 : 0);
                    }
                    if(moveA.equals("SCISSORS")) {
                        peersTable.get(idA).setRoundScore(moveB.equals("PAPER") ? 1 : 0);
                    }
                }
            }

            peersTable.get(idA).setTotalScore(peersTable.get(idA).getRoundScore());
            peersTable.get(idA).resetRoundScore();
        }

        String moveA = currentPeerInfo.getCurrentMove();

        for (String idB : peersTable.keySet()) {
            String moveB = peersTable.get(idB).getCurrentMove();

            if (moveA.equals("PAPER")) {
                currentPeerInfo.setRoundScore(moveB.equals("ROCK") ? 1 : 0);
            }
            if (moveA.equals("ROCK")) {
                currentPeerInfo.setRoundScore(moveB.equals("SCISSORS") ? 1 : 0);
            }
            if (moveA.equals("SCISSORS")) {
                currentPeerInfo.setRoundScore(moveB.equals("PAPER") ? 1 : 0);
            }
        }

        currentPeerInfo.setTotalScore(currentPeerInfo.getRoundScore());
    }

    private String printScore(PeerInfo peer) {
        return "Round score: " + peer.getRoundScore() + " - Total score: " + peer.getTotalScore();
    }

    private void resetPeersMoves(HashMap<String, PeerInfo> peersTable) {
        for (String id : peersTable.keySet()) {
            peersTable.get(id).setCurrentMove(null);
        }
    }

    private void resetCurrentPeer(PeerInfo peer) {
        peer.setCurrentMove(null);
        peer.resetRoundScore();
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

        @Override
        public PeerInfo getPeerInfo() {
            return currentPeerInfo;
        }

        @Override
        public void setPeerMove(String move, PeerInfo peer) {
            System.out.println("Move " + move + " by " + peer.getPort());
            peersTable.get(peer.getId()).setCurrentMove(move);

            checkEndGame();
        }
    }
}
