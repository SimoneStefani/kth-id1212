package peer.view;

import common.PrettyPrinter;
import peer.controller.Controller;
import peer.net.server.OutputHandler;

import java.util.Scanner;

public class GameShell implements Runnable {
    private static final String PROMPT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private boolean running = false;
    private Controller controller;

    public void start() {
        if (running) return;
        running = true;

        controller = new Controller(new ConsoleOutput());

        new Thread(this).start();
    }

    @Override
    public void run() {
        outMgr.print(PrettyPrinter.buildWelcomeMessage());
        outMgr.println(PrettyPrinter.buildStartInfoMessage());
        outMgr.print(PROMPT);

        while (running) {
            try {
                LineParser parsedLine = new LineParser(console.nextLine());
                switch (parsedLine.getCommand()) {
                    case CONNECT:
                        controller.joinNetwork(
                                parsedLine.getArgument(0),
                                Integer.parseInt(parsedLine.getArgument(1)));
                        break;
                    case QUIT:
                        controller.leaveNetwork();
                        running = false;
                        break;
                    case MAKE_MOVE:
                        controller.sendMove(parsedLine.getArgument(0), new ConsoleOutput());
                        break;
                    case NO_COMMAND:
                        outMgr.print(PROMPT );
                        break;
                }
            } catch (IllegalArgumentException e) {
                outMgr.print(PrettyPrinter.buildCommandErrorMessage(e.getMessage()));
                outMgr.print(PROMPT);
            }
        }
    }

    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMgr.println(msg);
            outMgr.print(PROMPT);
        }
    }
}
