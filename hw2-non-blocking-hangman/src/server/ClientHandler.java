package server;

import common.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

import static common.Message.deserialize;

public class ClientHandler implements Runnable {
    private final GameServer server;
    private final SocketChannel clientChannel;
    private final ByteBuffer clientMessage = ByteBuffer.allocateDirect(8192);
    private LinkedBlockingQueue<Message> messageQueue;

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
        clientMessage.clear();
        clientMessage.put("hello from server".getBytes());
        clientMessage.flip();
        clientChannel.write(clientMessage);
        clientMessage.clear();
    }

    private String extractMessageFromBuffer() {
        clientMessage.flip();
        byte[] bytes = new byte[clientMessage.remaining()];
        clientMessage.get(bytes);
        return new String(bytes);
    }

}
