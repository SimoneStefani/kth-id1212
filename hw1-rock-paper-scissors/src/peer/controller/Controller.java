package peer.controller;

import common.PeersTable;
import common.PrettyPrinter;
import peer.net.client.PeerConnection;
import peer.net.client.StartupServerConnection;
import peer.net.server.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private PeerInfo currentPeerInfo;
    private PeersTable peersTable;
    private OutputHandler console;
    private String startupServerHost = "127.0.0.1";
    private int startupServerPort = 8080;

    public Controller(OutputHandler console) {
        this.console = console;
        try {
            ServerSocket serverSocket = new ServerSocket(0); // The port will be automatically assigned
            this.currentPeerInfo = new PeerInfo(serverSocket.getInetAddress().getHostName(), serverSocket.getLocalPort());
            new PeerServer().start(serverSocket, new PeersTableManipulator());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void joinNetwork(String ip, int port) {
        this.startupServerHost = ip;
        this.startupServerPort = port;
        CompletableFuture.runAsync(() -> {
            try {
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                startupServerConnection.startConnection(ip, port);
                peersTable = startupServerConnection.sendJoinMessage(currentPeerInfo);
                startupServerConnection.stopConnection();
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            } catch (IOException e) {
                console.handleMsg(PrettyPrinter.buildNetworkErrorMessage("Unable to connect to startup server!"));
            }
        }).thenRun(() -> console.handleMsg(PrettyPrinter.buildSuccessfulConnectionMessage(peersTable.getTableSize())))
                .thenRun(this::contactJoinAllPeers);
    }

    private void contactJoinAllPeers() {
        for (PeerInfo peer : peersTable.getPeersInfo()) {
            CompletableFuture.runAsync(() -> {
                try {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection(peer.getHost(), peer.getPort());
                    PeerInfo syncedPeerInfo = peerConnection.sendJoinMessage(currentPeerInfo);
                    peersTable.replacePeer(syncedPeerInfo);
                    peerConnection.stopConnection();
                } catch (ClassNotFoundException | IOException e) {
                    if (e instanceof ConnectException) {
                        peersTable.removePeerFromTable(peer.getId());
                    }
                }
            });
        }
    }

    public void sendMove(String move, OutputHandler console) {
        if (currentPeerInfo.getCurrentMove() != null) return;
        currentPeerInfo.setCurrentMove(move);
        if (peersTable.getPeersTable().size() == 0) {
            console.handleMsg(PrettyPrinter.buildWaitingPeersMessage());
            return;
        }
        String message = GameManager.checkEndGame(peersTable, currentPeerInfo);
        if (!message.equals("")) console.handleMsg(PrettyPrinter.buildScoreMessage(message));

        for (PeerInfo peer : peersTable.getPeersInfo()) {
            CompletableFuture.runAsync(() -> {
                try {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection(peer.getHost(), peer.getPort());
                    peerConnection.sendMoveMessage(move, currentPeerInfo);
                    peerConnection.stopConnection();
                } catch (IOException | ClassNotFoundException e) {
                    if (e instanceof ConnectException) {
                        peersTable.removePeerFromTable(peer.getId());
                        String msg = GameManager.checkEndGame(peersTable, currentPeerInfo);
                        if (!msg.equals("")) console.handleMsg(PrettyPrinter.buildScoreMessage(msg));
                    }
                }
            });
        }
    }

    public void leaveNetwork() {
        CompletableFuture.runAsync(() -> {
            try {
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                startupServerConnection.startConnection(startupServerHost, startupServerPort);
                startupServerConnection.sendLeaveMessage(currentPeerInfo);
                startupServerConnection.stopConnection();

                for (PeerInfo peer : peersTable.getPeersInfo()) {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection(peer.getHost(), peer.getPort());
                    peerConnection.sendLeaveMessage(currentPeerInfo);
                    peerConnection.stopConnection();
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        });
    }

    private class PeersTableManipulator implements ControllerObserver {
        @Override
        public void addPeer(PeerInfo peer) {
            peersTable.addPeerToTable(peer);
        }

        @Override
        public void removePeer(PeerInfo peer) {
            peersTable.removePeerFromTable(peer.getId());

            String message = GameManager.checkEndGame(peersTable, currentPeerInfo);
            if (!message.equals("")) console.handleMsg(PrettyPrinter.buildScoreMessage(message));
        }

        @Override
        public PeerInfo getPeerInfo() {
            return currentPeerInfo;
        }

        @Override
        public void setPeerMove(String move, PeerInfo peer) {
            peersTable.setPeerMove(peer, move);

            String message = GameManager.checkEndGame(peersTable, currentPeerInfo);
            if (!message.equals("")) console.handleMsg(PrettyPrinter.buildScoreMessage(message));
        }
    }
}
