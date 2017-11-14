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

    public static String buildCommandErrorMessage(String reason) {
        return "\033[31m" + "Command error: " + reason + "\033[0m" + "\n";
    }

    public static String buildNetworkErrorMessage(String reason) {
        return "\033[31m" + "Network error: " + reason + "\033[0m" + "\n";
    }

    public static String buildScoreMessage(String message) {
        return "\033[36m" + TOP_BORDER + "\n" + VERTICAL_LINE + "  " + message + "\n" +
                BOTTOM_BORDER + "\n" + "\033[33m" +
                "A new qame has started; you can always quit with '\033[35mquit\033[33m'." + "\033[0m" + "\n";
    }

    public static String buildStartInfoMessage() {
        return "\033[33m" + "Connect to a P2P network with '\033[35mconnect <ip> <port>\033[33m'\n" +
                "Quit the game with '\033[35mquit\033[33m'" + "\033[0m" + "\n";
    }

    public static String buildWaitingPeersMessage() {
        return "\033[33m" + "You have made your move! Wait for others players to join the network!" + "\033[0m" + "\n";
    }

    public static String buildSuccessfulConnectionMessage(int peersNumber) {
        return "\033[33m" + "Connection successful! There are " + peersNumber +
                " players on this network.\n" + "Submit your move (rock/paper/scissors) " +
                "and wait for the other to make their move." + "\033[0m" + "\n";
    }
}
