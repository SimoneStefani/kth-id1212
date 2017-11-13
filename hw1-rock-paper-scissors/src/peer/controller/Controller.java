package peer.controller;

import common.PeersTable;
import peer.net.client.PeerConnection;
import peer.net.client.StartupServerConnection;
import peer.net.server.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private PeerInfo currentPeerInfo;
    private PeersTable peersTable;
    private OutputHandler console;

    public Controller(OutputHandler console) {
        this.console = console;
        try {
            ServerSocket serverSocket = new ServerSocket(0); // The port will be automatically assigned
            this.currentPeerInfo = new PeerInfo(serverSocket.getInetAddress().toString(), serverSocket.getLocalPort());
            new PeerServer().start(serverSocket, new PeersTableManipulator());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void joinNetwork() {
        CompletableFuture.runAsync(() -> {
            try {
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                startupServerConnection.startConnection("127.0.0.1", 8080);
                peersTable = startupServerConnection.sendJoinMessage(currentPeerInfo);
                startupServerConnection.stopConnection();

                for (PeerInfo peer : peersTable.getPeersInfo()) {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection("127.0.0.1", peer.getPort());
                    PeerInfo syncedPeerInfo = peerConnection.sendJoinMessage(currentPeerInfo);
                    peersTable.replacePeer(syncedPeerInfo);
                    peerConnection.stopConnection();
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        });
    }

    public void sendMove(String move, OutputHandler console) {
        // Check for already move
        currentPeerInfo.setCurrentMove(move);
        CompletableFuture.runAsync(() -> {
            try {
                for (PeerInfo peer : peersTable.getPeersInfo()) {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection("127.0.0.1", peer.getPort());
                    peerConnection.sendMoveMessage(move, currentPeerInfo);
                    peerConnection.stopConnection();
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }).thenRun(() -> console.handleMsg(GameManager.checkEndGame(peersTable, currentPeerInfo))); // TODO fix
    }

    public void leaveNetwork() {
        CompletableFuture.runAsync(() -> {
            try {
                StartupServerConnection startupServerConnection = new StartupServerConnection();
                startupServerConnection.startConnection("127.0.0.1", 8080);
                startupServerConnection.sendLeaveMessage(currentPeerInfo);
                startupServerConnection.stopConnection();

                for (PeerInfo peer : peersTable.getPeersInfo()) {
                    PeerConnection peerConnection = new PeerConnection();
                    peerConnection.startConnection("127.0.0.1", peer.getPort());
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
        }

        @Override
        public PeerInfo getPeerInfo() {
            return currentPeerInfo;
        }

        @Override
        public void setPeerMove(String move, PeerInfo peer) {
            peersTable.setPeerMove(peer, move);
            console.handleMsg(GameManager.checkEndGame(peersTable, currentPeerInfo));
        }
    }
}
