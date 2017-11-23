package client.controller;

import client.net.CommunicationListener;
import client.net.ServerConnection;

import java.io.IOException;

public class Controller {
    ServerConnection serverConnection = new ServerConnection();

    /**
     * Connect to a specific server
     * @param host
     * @param port
     */
    public void connect(String host, int port) {
        serverConnection.connect(host, port);
    }

    /**
     * Start a new hangman game
     */
    public void startNewRound() {
        serverConnection.startNewRound();
    }

    /**
     * Make a guess for the current word
     * @param guess
     */
    public void submitGuess(String guess) {
        serverConnection.submitGuess(guess);
    }

    /**
     * Quit and close connection
     * @throws IOException
     */
    public void disconnect() throws IOException {
        serverConnection.disconnect();
    }

    /*
     * Pass the view observer for console output
     */
    public void setViewObserver(CommunicationListener observer) {
        serverConnection.setViewObserver(observer);
    }
}
