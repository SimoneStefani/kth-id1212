package server;

import java.nio.channels.SocketChannel;

public class ClientHandler {
    private final GameServer server;
    private final SocketChannel clientChannel;

    ClientHandler(GameServer server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
    }
}
