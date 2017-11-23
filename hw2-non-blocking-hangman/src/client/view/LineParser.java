package client.view;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class LineParser {
    private final String IP_REGEX = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
    private final String PORT_REGEX = "(6553[0-5]|655[0-2][0-9]\\d|65[0-4](\\d){2}|6[0-4](\\d){3}|[1-5](\\d){4}|[1-9](\\d){0,3})";
    private ArrayList<String> arguments = new ArrayList<>();
    private Command command;

    public LineParser(String rawLine) {
        determineCommand(rawLine);
    }

    private void determineCommand(String rawLine) {
        StringTokenizer stringTokenizer = new StringTokenizer(rawLine);

        if (stringTokenizer.countTokens() == 0) {
            this.command = Command.NO_COMMAND;
            return;
        }

        String cmd = stringTokenizer.nextToken().toUpperCase();
        switch (cmd) {
            case "CONNECT":
                if (!stringTokenizer.hasMoreTokens()) {
                    throw new IllegalArgumentException("Missing IP address!");
                }
                this.command = Command.CONNECT;
                String ip = stringTokenizer.nextToken();
                if (!Pattern.matches(IP_REGEX, ip)) {
                    throw new IllegalArgumentException("Invalid IP address!");
                }
                arguments.add(ip);
                if (!stringTokenizer.hasMoreTokens()) {
                    throw new IllegalArgumentException("Missing port!");
                }
                String port = stringTokenizer.nextToken();
                if (!Pattern.matches(PORT_REGEX, port)) {
                    throw new IllegalArgumentException("Invalid port!");
                }
                arguments.add(port); // Port
                break;
            case "QUIT":
                this.command = Command.QUIT;
                break;
            case "START":
                this.command = Command.START;
                break;
            default:
                this.command = Command.GUESS;
                arguments.add(cmd); // Guess
        }
    }

    public String getArgument(int index) {
        return arguments.get(index);
    }

    public Command getCommand() {
        return command;
    }
}
