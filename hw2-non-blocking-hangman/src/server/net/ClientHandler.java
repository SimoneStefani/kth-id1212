package server.net;

import common.Message;
import common.MessageType;

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
    private final LinkedBlockingQueue<Message> messageQueue;
    private SelectionKey selectionKey;

    ClientHandler(GameServer server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        Iterator<Message> iterator = messageQueue.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            switch (message.getMessageType()) {
                case START:
                    System.out.println("Msg: START");
                    try {
                        writeMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case GUESS:
                    System.out.println("Msg: GUESS: " + message.getBody());
                    break;
                case QUIT:
                    System.out.println("Msg: QUIT");
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

        messageQueue.add(deserialize(extractMessageFromBuffer()));
        ForkJoinPool.commonPool().execute(this);
    }

    public void writeMessage() throws IOException {
        synchronized (messageQueue) {
            while (messageQueue.size() > 0) {
                ByteBuffer message = ByteBuffer.wrap(serialize(messageQueue.poll()).getBytes());
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

    private String extractMessageFromBuffer() {
        clientMessage.flip();
        byte[] bytes = new byte[clientMessage.remaining()];
        clientMessage.get(bytes);
        return new String(bytes);
    }


    private void sendResponseToClient(String body) {
        Message message = new Message(MessageType.RESPONSE, body);

        synchronized (messageQueue) {
            messageQueue.add(message);
        }

        server.addMessageToWritingQueue(this.selectionKey);
        server.wakeupSelector();
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }
}
