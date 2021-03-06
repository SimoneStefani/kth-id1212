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

    public static String buildSimpleMessage(String message) {
        return "\033[33m" + message + "\033[0m" + "\n";
    }

    public static String buildSimpleErrorMessage(String message) {
        return "\033[31m" + message + "\033[0m" + "\n";
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
                VERTICAL_LINE + " '\033[35mupload <name> <private(t/f)> <write(t/f)> <read(t/f)>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mdownload <name>\033[36m' \n" +
                VERTICAL_LINE + " '\033[35mlist\033[36m' \n" +
                BOTTOM_BORDER + "\033[0m" + "\n";
    }
}