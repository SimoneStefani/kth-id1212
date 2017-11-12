package peer.net.server;

public class PeerInfo {
    private String id;
    private String host;
    private int port;

    public PeerInfo(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return id;
    }
}
