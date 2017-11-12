package peer.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {

    public void fetchPeerList() {
        CompletableFuture.supplyAsync(() -> {
            try {
                // Fetch list of peers from startup server
                // 1) Create StartupServerConnection
                // 2) Connect to startup server
                // 3) Fetch list of peers
                // 4) Return list of peers
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public void joinNetwork() {
        CompletableFuture.runAsync(() -> {
            try {
                // Foreach peer in list send join message
                // 1) Create new PeerClient
                // 2) Connect to peer
                // 3) Send join so that peer can add to list
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public void sendMove(String move) {
        CompletableFuture.runAsync(() -> {
            try {
                // Foreach peer in list send move
                // 1) Create new PeerClient
                // 2) Connect to peer
                // 3) Send move
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public void leaveNetwork() {
        CompletableFuture.runAsync(() -> {
            try {
                // Foreach peer in list send leave message
                // 1) Create new PeerClient
                // 2) Connect to peer
                // 3) Send join so that peer can remove from list
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
