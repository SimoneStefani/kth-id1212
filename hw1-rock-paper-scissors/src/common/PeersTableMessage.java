package common;

import peer.net.server.PeerInfo;

import java.io.Serializable;
import java.util.HashMap;

public class PeersTableMessage implements Serializable {
    HashMap<String, PeerInfo> peersTable;

    public PeersTableMessage(HashMap<String, PeerInfo> peersTable) {
        this.peersTable = peersTable;
    }

    public HashMap<String, PeerInfo> getPeersTable() {
        return peersTable;
    }
}
