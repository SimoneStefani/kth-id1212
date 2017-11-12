package peer.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerServer implements Runnable {
    private ServerSocket serverSocket;

    public void start(ServerSocket serverSocket) {
        System.out.println("Starting peer server...");
        this.serverSocket = serverSocket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Running peer server...");
        try {
            System.out.println("new peer socket: " + serverSocket);
            while (true) new PeerClientHandler(serverSocket.accept()).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void stop() {
//        try {
//            serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private static class PeerClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public PeerClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
