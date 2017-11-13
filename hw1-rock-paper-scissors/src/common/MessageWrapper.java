package common;

import peer.net.server.PeerInfo;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private Message message;
    private PeerInfo senderPeerInfo;
    private String move;

    public MessageWrapper(Message message, PeerInfo senderPeerInfo) {
        this.message = message;
        this.senderPeerInfo = senderPeerInfo;
        this.move = null;
    }

    public MessageWrapper(Message Message, PeerInfo senderPeerInfo, String move) {
        this.message = message;
        this.senderPeerInfo = senderPeerInfo;
        this.move = move;
    }

    public Message getMessage() {
        return message;
    }

    public PeerInfo getSenderPeerInfo() {
        return senderPeerInfo;
    }

    public String getMove() {
        return move;
    }
}
