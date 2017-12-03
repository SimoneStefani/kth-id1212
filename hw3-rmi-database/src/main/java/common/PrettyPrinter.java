package common;

public class PrettyPrinter {
    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char VERTICAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "─────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    public static String buildWelcomeMessage() {
        return "\033[36m" + TOP_BORDER + "\n" +
                VERTICAL_LINE + "  WELCOME TO THE FILE CATALOG \n" +
                BOTTOM_BORDER + "\033[0m" + "\n";
    }

    public static String buildCommandErrorMessage(String reason) {
        return "\033[31m" + "Command error: " + reason + "\033[0m" + "\n";
    }

    public static String buildNetworkErrorMessage(String reason) {
        return "\033[31m" + "Network error: " + reason + "\033[0m" + "\n";
    }

    public static String buildGuessResponseMessage(String message) {
        return "\033[36m" + message + "\033[0m" + "\n";
    }

    public static String buildEndResponseMessage(String message) {
        return "\033[36m" + TOP_BORDER + "\n" + VERTICAL_LINE + " The game is ended! Here is the result:" +
                "\n" + VERTICAL_LINE + " " + message + "\n" + BOTTOM_BORDER + "\n" + "\033[33m" +
                "You can start a new game with '\033[35mstart\033[33m'" + "\033[0m" + "\n";
    }

    public static String buildMakeGuessMessage(String message) {
        return "\033[36m" + TOP_BORDER + "\n" + VERTICAL_LINE + " You have started a new game!" + "\n" + MIDDLE_BORDER +
                "\n" + VERTICAL_LINE + " " + message + "\n" + BOTTOM_BORDER + "\n" + "\033[33m" +
                "Go on and type a guess for a letter or the word; you can always quit with '\033[35mquit\033[33m'." +
                "\033[0m" + "\n";
    }

    public static String buildStartInfoMessage() {
        return "\033[33m" + "Type '\033[35mhelp\033[33m' to learn about available commands!\n" +
                "Quit the session with '\033[35mquit\033[33m'" + "\033[0m" + "\n";
    }

    public static String buildHelpMessage() {
        return "\033[36m" + TOP_BORDER + "\n" +
                VERTICAL_LINE + " FILE CATALOG HELP MENU \n" + MIDDLE_BORDER + "\n" +
                VERTICAL_LINE + " '\033[35mregister <username> <password>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35munregister <username> <password>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mlogin <username> <password>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mlogout\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mupload <name> <permission(true/false)>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mlist\033[36m' \n" +
                BOTTOM_BORDER + "\033[0m" + "\n";
    }
}