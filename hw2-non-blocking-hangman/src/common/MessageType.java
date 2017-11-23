package common;

public enum MessageType {
    /*
     * Start a new Hangman game
     */
    START,

    /*
     * Submit a new guess
     */
    GUESS,

    /*
     * Quit the game and close connection
     */
    QUIT,

    /*
     * Server response after the game is started
     */
    START_RESPONSE,

    /*
     * Server response after a guess
     */
    GUESS_RESPONSE,

    /*
     * Server response after a game is ended
     */
    END_RESPONSE
}
