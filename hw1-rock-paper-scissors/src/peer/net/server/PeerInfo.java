package peer.net.server;

import java.io.Serializable;

public class PeerInfo implements Serializable {
    private String id;
    private String host;
    private int port;

    private String currentMove;
    private int roundScore;
    private int totalScore;

    public PeerInfo(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;

        this.currentMove = null;
        this.roundScore = 0;
        this.totalScore = 0;
    }

    public PeerInfo(String host, int port) {
        this(host + ":" + port, host, port);
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getCurrentMove() {
        return currentMove;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCurrentMove(String currentMove) {
        this.currentMove = currentMove;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore += roundScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore += totalScore;
    }

    public void resetRoundScore() {
        this.roundScore = 0;
    }

    public void resetTotalScore() {
        this.totalScore = 0;
    }

    @Override
    public String toString() {
        return id;
    }
}
