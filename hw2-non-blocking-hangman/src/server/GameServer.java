package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class GameServer {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private int port;

    public GameServer(int port) throws IOException {
        this.port = port;
        this.selector = this.initSelector();
    }

    public static void main(String[] args) throws IOException {
        int portNum = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        new GameServer(portNum).run();
    }

    private Selector initSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        this.serverChannel.socket().bind(new InetSocketAddress(this.port));
        this.serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }

    public void run() {
        while (true) {
            try {
                this.selector.select();

                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) startHandler(key);
                    else if (key.isReadable()) readFromClient(key);
                    else if (key.isWritable()) writeToClient(key);

                    selectedKeys.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        ClientHandler handler = new ClientHandler(this, socketChannel);
        socketChannel.register(selector, SelectionKey.OP_WRITE, new Client(handler));
    }

    private void readFromClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
    }

    private void writeToClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
    }

    private void removeClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
    }

    private class Client {
        private final ClientHandler handler;

        private Client(ClientHandler handler) {
            this.handler = handler;
        }
    }
}
