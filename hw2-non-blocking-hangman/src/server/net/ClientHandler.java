package server.net;

import common.Message;
import common.MessageType;
import server.model.Game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

import static common.Message.deserialize;
import static common.Message.serialize;

public class ClientHandler implements Runnable {
    private final GameServer server;
    private final SocketChannel clientChannel;
    private final ByteBuffer clientMessage = ByteBuffer.allocateDirect(8192);
    private final LinkedBlockingQueue<Message> readingQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Message> sendingQueue = new LinkedBlockingQueue<>();

    private SelectionKey selectionKey; // Selection key under which this handler is registered
    private Game hangmanGame = new Game(); // Instance of the game

    ClientHandler(GameServer server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        Iterator<Message> iterator = readingQueue.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            switch (message.getMessageType()) {
                case START:
                    String currentState = this.hangmanGame.startRound();
                    sendResponseToClient(MessageType.START_RESPONSE, currentState);
                    break;
                case GUESS:
                    String currentState1 = this.hangmanGame.validateGuess(message.getBody());
                    if (this.hangmanGame.getChosenWord() == null) {
                        sendResponseToClient(MessageType.END_RESPONSE, currentState1);
                    } else {
                        sendResponseToClient(MessageType.GUESS_RESPONSE, currentState1);
                    }
                    break;
                case QUIT:
                    disconnectClient();
                    break;
                default:
                    System.out.println("Message type non available!");
            }
            iterator.remove();
        }
    }

    /*
     * Read message from channel into buffer and add to
     * reading queue. Process message in another thread.
     */
    void readMessage() throws IOException {
        clientMessage.clear();
        int numOfReadBytes = clientChannel.read(clientMessage);
        if (numOfReadBytes == -1) throw new IOException("Client has closed connection.");

        readingQueue.add(deserialize(extractMessageFromBuffer()));
        ForkJoinPool.commonPool().execute(this);
    }

    /*
     * Load message from sending queue into buffer and
     * write it to client channel.
     */
    void writeMessage() throws IOException {
        synchronized (sendingQueue) {
            while (sendingQueue.size() > 0) {
                ByteBuffer message = ByteBuffer.wrap(serialize(sendingQueue.poll()).getBytes());
                clientChannel.write(message);
            }
        }
    }

    /*
     * Close channel with client.
     */
    void disconnectClient() {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Set the selection key under which the handler is
     * registered. This is useful for the server to direct
     * incoming requests to the correct handler.
     */
    void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    private String extractMessageFromBuffer() {
        clientMessage.flip();
        byte[] bytes = new byte[clientMessage.remaining()];
        clientMessage.get(bytes);
        return new String(bytes);
    }

    /*
     * Build a message and add it to the sending queue.
     * Then transfer it to the server writing queue and
     * explicitly wake up selector to process it.
     */
    private void sendResponseToClient(MessageType messageType, String body) {
        Message message = new Message(messageType, body);

        synchronized (sendingQueue) {
            sendingQueue.add(message);
        }

        server.addMessageToWritingQueue(this.selectionKey);
        server.wakeupSelector();
    }
}
