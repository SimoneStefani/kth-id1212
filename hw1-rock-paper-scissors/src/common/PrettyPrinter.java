package common;

public class PrettyPrinter {
    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char VERTICAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER;

    public static String buildWelcomeMessage() {
        return "\033[36m" + TOP_BORDER + "\n" +
                VERTICAL_LINE + "  WELCOME TO P2P ROCK-PAPER-SCISSORS \n" +
                BOTTOM_BORDER + "\033[0m" + "\n";
    }

//    public static String buildPeerJoinedMessage(int peersNumber) {
//        return "\033[33m" + "A new player has joined the network." + "\033[0m" +
//                buildNumberPeersMessage(peersNumber);
//    }
//
//    public static String buildNumberPeersMessage(int peersNumber) {
//        return "\033[33m" + "There are " + peersNumber + "peers connected!";
//    }

    public static String buildStartInfoMessage() {
        return "\033[33m" + "Connect to a P2P network with '\033[35mconnect <ip> <port>\033[33m'\n" +
                "Disconnect from the network with '\033[35mdisconnect\033[33m'" + "\033[0m" + "\n";
    }

    public static String buildSuccessfulConnectionMessage(int peersNumber) {
        return "\033[33m" + "Connection successful! There are " + peersNumber +
                " players on this network.\n" + "Submit your move (rock/paper/scissors) " +
                "and wait for the other to make their move." + "\033[0m" + "\n";
    }
}
