package client.net;

import common.Message;
import common.MessageType;
import common.PrettyPrinter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.LinkedBlockingQueue;

import static common.Message.deserialize;
import static common.Message.serialize;

public class ServerConnection implements Runnable {
    private final ByteBuffer serverMessage = ByteBuffer.allocateDirect(8192);
    private final LinkedBlockingQueue<Message> sendingQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Message> readingQueue = new LinkedBlockingQueue<>();
    private CommunicationListener viewObserver;
    private volatile boolean timeToSend = false;
    private InetSocketAddress serverAddress;
    private SocketChannel socketChannel;
    private boolean connected = false;
    private Selector selector;

    public void connect(String host, int port) {
        this.serverAddress = new InetSocketAddress(host, port);
        new Thread(this).start();
    }

    public void startNewRound() {
        enqueueAndSendMessage(MessageType.START, "");
    }

    public void submitGuess(String guess) {
        enqueueAndSendMessage(MessageType.GUESS, guess);
    }

    public void disconnect() throws IOException {
        this.connected = false;
        enqueueAndSendMessage(MessageType.QUIT, "");
        this.socketChannel.close();
        this.socketChannel.keyFor(selector).cancel();
    }

    public void setViewObserver(CommunicationListener observer) {
        this.viewObserver = observer;
    }

    @Override
    public void run() {
        try {
            initSelector();

            while (connected) {
                if (timeToSend) {
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    timeToSend = false;
                }

                this.selector.select();

                for (SelectionKey key : this.selector.selectedKeys()) {

                    if (!key.isValid()) continue;

                    if (key.isConnectable()) establishConnection(key);
                    else if (key.isReadable()) readFromServer(key);
                    else if (key.isWritable()) writeToServer(key);

                    selector.selectedKeys().remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSelector() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();

        this.socketChannel = SocketChannel.open();
        this.socketChannel.configureBlocking(false);
        this.socketChannel.connect(serverAddress);
        this.socketChannel.register(selector, SelectionKey.OP_CONNECT);

        this.connected = true;
    }

    private void enqueueAndSendMessage(MessageType messageType, String body) {
        Message message = new Message(messageType, body);

        synchronized (sendingQueue) {
            sendingQueue.add(message);
        }

        this.timeToSend = true;
        selector.wakeup();
    }

    private void establishConnection(SelectionKey key) throws IOException {
        this.socketChannel.finishConnect();
        viewObserver.print(PrettyPrinter.buildStartGameMessage());
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void readFromServer(SelectionKey key) throws IOException {
        serverMessage.clear();
        int numOfReadBytes = socketChannel.read(serverMessage);
        if (numOfReadBytes == -1) throw new IOException("Client has closed connection!");

        readingQueue.add(deserialize(extractMessageFromBuffer()));

        while (readingQueue.size() > 0) {
            Message message = readingQueue.poll();

            switch (message.getMessageType()) {
                case START_RESPONSE:
                    viewObserver.print(PrettyPrinter.buildMakeGuessMessage(message.getBody()));
                    break;
                case GUESS_RESPONSE:
                    viewObserver.print(PrettyPrinter.buildGuessResponseMessage(message.getBody()));
                    break;
                case END_RESPONSE:
                    viewObserver.print(PrettyPrinter.buildEndResponseMessage(message.getBody()));
                    break;
                default:
                    viewObserver.print(message.getBody());
            }
        }
    }

    private void writeToServer(SelectionKey key) throws IOException {
        synchronized (sendingQueue) {
            while (sendingQueue.size() > 0) {
                ByteBuffer message = ByteBuffer.wrap(serialize(sendingQueue.poll()).getBytes());
                socketChannel.write(message);
                if (message.hasRemaining()) return;
            }
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    private String extractMessageFromBuffer() {
        serverMessage.flip();
        byte[] bytes = new byte[serverMessage.remaining()];
        serverMessage.get(bytes);
        return new String(bytes);
    }

}
