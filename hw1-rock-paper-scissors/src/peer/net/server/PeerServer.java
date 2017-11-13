package peer.net.server;

import peer.controller.Controller;
import protocol.UtilityMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerServer implements Runnable {
    private ServerSocket serverSocket;
    private ControllerObserver controllerObserver;

    public void start(ServerSocket serverSocket, ControllerObserver controllerObserver) {
        System.out.println("Starting peer server...");
        this.serverSocket = serverSocket;
        this.controllerObserver = controllerObserver;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Running peer server...");
        try {
            System.out.println("new peer socket: " + serverSocket);
            while (true) new PeerClientHandler(serverSocket.accept(), controllerObserver).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void stop() {
//        try {
//            serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private static class PeerClientHandler extends Thread {
        private final ControllerObserver controllerObserver;
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public PeerClientHandler(Socket socket, ControllerObserver controllerObserver) {
            this.clientSocket = socket;
            this.controllerObserver = controllerObserver;
        }

        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                UtilityMessage message = (UtilityMessage) in.readObject();

                if (message.getMessage().equals("JOIN")) {
                    System.out.println("Joining request: " + message.getSenderPeerInfo().getPort());
                    controllerObserver.addPeer(message.getSenderPeerInfo());
                    out.writeObject(new UtilityMessage("SYNC", controllerObserver.getPeerInfo()));
                } else if (message.getMessage().equals("LEAVE")) {
                    System.out.println("Leave request: " + message.getSenderPeerInfo().getPort());
                    controllerObserver.removePeer(message.getSenderPeerInfo());
                } else if (message.getMessage().equals("MOVE")) {
                    System.out.println("Move request: " + message.getMove() + " - " + message.getSenderPeerInfo().getPort());
                    controllerObserver.setPeerMove(message.getMove(), message.getSenderPeerInfo());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
