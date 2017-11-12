package startupserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StartupServer {
    private ServerSocket serverSocket;
    // List of peers in network

    public static void main(String[] args) throws IOException {
        StartupServer server = new StartupServer();
        server.start(5555);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) new StartupPeerHandler(serverSocket.accept()).run();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class StartupPeerHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public StartupPeerHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // If message JOIN, add to list and return list
                // If message LEAVE, remove from list

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
