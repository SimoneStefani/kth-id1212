package peer.net.server;

public interface ControllerObserver {
    public void addPeer(PeerInfo peer);
    public void removePeer(PeerInfo peer);
}
