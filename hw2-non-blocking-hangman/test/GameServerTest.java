import org.junit.Before;
import org.junit.Test;
import server.GameServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class GameServerTest {
    GameServer gameServer;

    @Before
    public void setup() throws IOException, InterruptedException {
        gameServer = new GameServer(8080);
        gameServer.run();
    }

    @Test
    public void send_message_to_server() throws IOException {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer = ByteBuffer.wrap("START##Hello pews!".getBytes());
        client.write(buffer);
        assert(true);
    }
}
