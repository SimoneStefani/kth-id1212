package common;

import peer.net.server.PeerInfo;

import java.io.Serializable;

public class UtilityMessage implements Serializable {
    private String message;
    private PeerInfo senderPeerInfo;
    private String move;

    public UtilityMessage(String message, PeerInfo senderPeerInfo) {
        this.message = message;
        this.senderPeerInfo = senderPeerInfo;
        this.move = null;
    }

    public UtilityMessage(String message, PeerInfo senderPeerInfo, String move) {
        this.message = message;
        this.senderPeerInfo = senderPeerInfo;
        this.move = move;
    }

    public String getMessage() {
        return message;
    }

    public PeerInfo getSenderPeerInfo() {
        return senderPeerInfo;
    }

    public String getMove() {
        return move;
    }
}
