package common;

import peer.net.server.PeerInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class PeersTable implements Serializable {
    private HashMap<String, PeerInfo> peersTable;

    public PeersTable() {
        this.peersTable = new HashMap<>();
    }

    public void addPeerToTable(PeerInfo peerInfo) {
        this.peersTable.put(peerInfo.getId(), peerInfo);
    }

    public void removePeerFromTable(String id) {
        this.peersTable.remove(id);
    }

    public Collection<PeerInfo> getPeersInfo() {
        return this.peersTable.values();
    }

    public HashMap<String, PeerInfo> getPeersTable() {
        return peersTable;
    }

    public void replacePeer(PeerInfo peerInfo) {
        this.peersTable.replace(peerInfo.getId(), peerInfo);
    }

    public void resetPeersMoves() {
        for (PeerInfo peer : this.peersTable.values()) {
            peer.setCurrentMove(null);
        }
    }

    public boolean allPeersPlayed() {
        boolean allPlayed = true;

        for (PeerInfo peer : this.peersTable.values()) {
            if (peer.getCurrentMove() == null) allPlayed = false;
        }

        return allPlayed;
    }

    public void setPeerMove(PeerInfo peer, String move) {
        peersTable.get(peer.getId()).setCurrentMove(move);
    }

    @Override
    public String toString() {
        return "PeersTable: {" + peersTable + "}";
    }
}
