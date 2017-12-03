package client.view;

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
            case "REGISTER":
                this.command = Command.REGISTER;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing username");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing password");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "UNREGISTER":
                this.command = Command.UNREGISTER;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing username");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing password");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "LOGIN":
                this.command = Command.LOGIN;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing username");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing password");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "LOGOUT":
                this.command = Command.LOGOUT;
                break;
            case "UPLOAD":
                this.command = Command.STORE_FILE;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing name");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing permission");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing public write");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing public read");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "DOWNLOAD":
                this.command = Command.GET_FILE;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing file name");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "UPDATE":
                this.command = Command.UPDATE_FILE;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing name");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing permission");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing public write");
                arguments.add(stringTokenizer.nextToken());
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing public read");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "REMOVE":
                this.command = Command.DELETE_FILE;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing file name");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "LIST":
                this.command = Command.LIST_FILES;
                break;
            case "NOTIFY":
                this.command = Command.NOTIFY;
                if (!stringTokenizer.hasMoreTokens()) throw new IllegalArgumentException("Missing file name");
                arguments.add(stringTokenizer.nextToken());
                break;
            case "QUIT":
                this.command = Command.QUIT;
                break;
            default:
                this.command = Command.HELP;
        }
    }

    public String getArgument(int index) {
        return arguments.get(index);
    }

    public Command getCommand() {
        return command;
    }
}