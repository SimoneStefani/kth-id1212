package peer.net.server;

import common.Message;
import common.MessageWrapper;
import startupserver.PeerHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class PeerClientHandler {
    private static final Logger LOGGER = Logger.getLogger(PeerHandler.class.getName());
    private final ControllerObserver controllerObserver;
    private Socket clientSocket;

    PeerClientHandler(Socket socket, ControllerObserver controllerObserver) {
        this.clientSocket = socket;
        this.controllerObserver = controllerObserver;
    }

    void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            MessageWrapper message = (MessageWrapper) in.readObject();

            switch (message.getMessage()) {
                case JOIN:
                    controllerObserver.addPeer(message.getSenderPeerInfo());
                    out.writeObject(new MessageWrapper(Message.SYNC, controllerObserver.getPeerInfo()));
                    break;
                case LEAVE:
                    controllerObserver.removePeer(message.getSenderPeerInfo());
                    break;
                case MOVE:
                    controllerObserver.setPeerMove(message.getMove(), message.getSenderPeerInfo());
                    break;
                default:
                    LOGGER.log(Level.SEVERE, "Unrecognized command!");
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
