package protocol;

import peer.net.server.PeerInfo;

import java.io.Serializable;
import java.util.HashMap;

public class PeersListMessage implements Serializable {
    HashMap<String, PeerInfo> peersTable;

    public PeersListMessage(HashMap<String, PeerInfo> peersTable) {
        this.peersTable = peersTable;
    }

    public HashMap<String, PeerInfo> getPeersTable() {
        return peersTable;
    }
}
