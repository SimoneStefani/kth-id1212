package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestClient {
    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static TestClient instance;

    public static TestClient start() {
        if (instance == null) instance = new TestClient();

        return instance;
    }

    public static void stop() throws IOException {
        client.close();
        buffer = null;
    }

    private TestClient() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            buffer = ByteBuffer.allocate(8192);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = buffer.toString();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    public static void main(String[] args) {
        TestClient testClient = TestClient.start();
        String resp1 = testClient.sendMessage("START##hello");
        //String resp2 = testClient.sendMessage("GUESS##hello");
    }
}
