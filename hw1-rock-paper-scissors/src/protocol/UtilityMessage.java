package protocol;

import peer.net.server.PeerInfo;

import java.io.Serializable;

public class UtilityMessage implements Serializable {
    private String message;
    private PeerInfo senderPeerInfo;

    public UtilityMessage(String message, PeerInfo senderPeerInfo) {
        this.message = message;
        this.senderPeerInfo = senderPeerInfo;
    }

    public String getMessage() {
        return message;
    }

    public PeerInfo getSenderPeerInfo() {
        return senderPeerInfo;
    }
}
