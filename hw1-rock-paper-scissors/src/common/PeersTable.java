package common;

import peer.net.server.PeerInfo;

import java.util.HashMap;

public class PeersTable {
    private HashMap<String, PeerInfo> peersTable;

    /**
     * Create a new empty peersTable.
     */
    public PeersTable() {
        this.peersTable = new HashMap<>();
    }

    /**
     * Add a new peer to the table.
     *
     * @param peerInfo the new peer
     * @return the updated peersTable
     */
    public HashMap<String, PeerInfo> addPeerToTable(PeerInfo peerInfo) {
        this.peersTable.put(peerInfo.getId(), peerInfo);
        return peersTable;
    }

    /**
     * Remove a peer from the table.
     *
     * @param id of the peer to remove
     */
    public void removePeerFromTable(String id) {
        this.peersTable.remove(id);
    }
}
