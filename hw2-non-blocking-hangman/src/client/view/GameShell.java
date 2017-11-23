package client.view;

import client.controller.Controller;
import client.net.CommunicationListener;
import common.PrettyPrinter;

import java.io.IOException;
import java.util.Scanner;

public class GameShell implements Runnable {
    private static final String PROMPT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private ConsoleOutput consoleOutput = new ConsoleOutput();
    private boolean running = false;
    private Controller controller;

    public void start() {
        if (running) return;
        running = true;

        controller = new Controller();
        controller.setViewObserver(consoleOutput);

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
                        controller.connect(
                                parsedLine.getArgument(0),
                                Integer.parseInt(parsedLine.getArgument(1)));
                        break;
                    case QUIT:
                        controller.disconnect();
                        running = false;
                        break;
                    case START:
                        controller.startNewRound();
                        break;
                    case GUESS:
                        controller.submitGuess(parsedLine.getArgument(0));
                        break;
                    case NO_COMMAND:
                        outMgr.print(PROMPT );
                        break;
                }
            } catch (IllegalArgumentException | IOException e) {
                outMgr.print(PrettyPrinter.buildCommandErrorMessage(e.getMessage()));
                outMgr.print(PROMPT);
            }
        }
    }

    private class ConsoleOutput implements CommunicationListener {
        @Override
        public void print(String msg) {
            outMgr.println(msg);
            outMgr.print(PROMPT);
        }
    }
}
