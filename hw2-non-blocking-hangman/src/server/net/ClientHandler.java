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

    private SelectionKey selectionKey;
    private Game hangmanGame = new Game();

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
                    System.out.println("Msg: START");
                    String currentState = this.hangmanGame.startRound();
                    sendResponseToClient(currentState);
                    break;
                case GUESS:
                    System.out.println("Msg: GUESS: " + message.getBody());
                    String currentState1 = this.hangmanGame.validateGuess(message.getBody());
                    sendResponseToClient(currentState1);
                    break;
                case QUIT:
                    System.out.println("Msg: QUIT");
                    disconnectClient();
                    break;
                default:
                    System.out.println("Msg: ERROR");
            }
            iterator.remove();
        }
    }

    public void readMessage() throws IOException {
        clientMessage.clear();
        int numOfReadBytes = clientChannel.read(clientMessage);
        if (numOfReadBytes == -1) throw new IOException("Client has closed connection.");

        readingQueue.add(deserialize(extractMessageFromBuffer()));
        ForkJoinPool.commonPool().execute(this);
    }

    public void writeMessage() throws IOException {
        synchronized (sendingQueue) {
            while (sendingQueue.size() > 0) {
                ByteBuffer message = ByteBuffer.wrap(serialize(sendingQueue.poll()).getBytes());
                clientChannel.write(message);
            }
        }
    }

    public void disconnectClient() {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    private String extractMessageFromBuffer() {
        clientMessage.flip();
        byte[] bytes = new byte[clientMessage.remaining()];
        clientMessage.get(bytes);
        return new String(bytes);
    }


    private void sendResponseToClient(String body) {
        Message message = new Message(MessageType.RESPONSE, body);

        synchronized (sendingQueue) {
            sendingQueue.add(message);
        }

        server.addMessageToWritingQueue(this.selectionKey);
        server.wakeupSelector();
    }
}
