package peer.controller;

import common.PeersTable;
import peer.net.server.PeerInfo;

public class GameManager {

    public static String checkEndGame(PeersTable peersTable, PeerInfo currentPeerInfo) {
        String printout = "";

        if (peersTable.allPeersPlayed() && currentPeerInfo.getCurrentMove() != null) {
            calculateScore(peersTable, currentPeerInfo);
            printout = "Round score: " + currentPeerInfo.getRoundScore() + " - Total score: " + currentPeerInfo.getTotalScore();
            peersTable.resetPeersMoves();
            currentPeerInfo.setCurrentMove(null);
            currentPeerInfo.resetRoundScore();
        }

        return  printout;
    }

    private static void calculateScore(PeersTable peersTable, PeerInfo currentPeerInfo) {
        for (PeerInfo peerA : peersTable.getPeersInfo()) {
            String moveA = peerA.getCurrentMove();

            for (PeerInfo peerB : peersTable.getPeersInfo()) {
                if (!peerB.getId().equals(peerA.getId())) {
                    String moveB = peerB.getCurrentMove();

                    if(moveA.equals("PAPER")) {
                        peerA.setRoundScore(moveB.equals("ROCK") ? 1 : 0);
                    }
                    if(moveA.equals("ROCK")) {
                        peerA.setRoundScore(moveB.equals("SCISSORS") ? 1 : 0);
                    }
                    if(moveA.equals("SCISSORS")) {
                        peerA.setRoundScore(moveB.equals("PAPER") ? 1 : 0);
                    }
                }
            }

            peerA.setTotalScore(peerA.getRoundScore());
            peerA.resetRoundScore();
        }

        String moveA = currentPeerInfo.getCurrentMove();

        for (PeerInfo peerB : peersTable.getPeersInfo()) {
            String moveB = peerB.getCurrentMove();

            if (moveA.equals("PAPER")) {
                currentPeerInfo.setRoundScore(moveB.equals("ROCK") ? 1 : 0);
            }
            if (moveA.equals("ROCK")) {
                currentPeerInfo.setRoundScore(moveB.equals("SCISSORS") ? 1 : 0);
            }
            if (moveA.equals("SCISSORS")) {
                currentPeerInfo.setRoundScore(moveB.equals("PAPER") ? 1 : 0);
            }
        }

        currentPeerInfo.setTotalScore(currentPeerInfo.getRoundScore());
    }
}
