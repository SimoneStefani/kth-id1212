package peer.view;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class LineParser {
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
                this.command = Command.CONNECT;
                arguments.add(stringTokenizer.nextToken()); // IP Address
                arguments.add(stringTokenizer.nextToken()); // Port
                break;
            case "DISCONNECT":
                this.command = Command.DISCONNECT;
                break;
            default:
                if (!"ROCK".equals(cmd) && !"PAPER".equals(cmd) && !"SCISSORS".equals(cmd)) {
                    throw new IllegalArgumentException("Invalid move '" + cmd + "'!");
                }
                this.command = Command.MAKE_MOVE;
                arguments.add(cmd); // Move
        }
    }

    public String getArgument(int index) {
        return arguments.get(index);
    }

    public Command getCommand() {
        return command;
    }
}
