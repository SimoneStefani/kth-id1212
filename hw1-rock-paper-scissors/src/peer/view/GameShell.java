package peer.view;

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

        controller = new Controller();

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            CmdLine cmdLine = new CmdLine(readNextLine());
            switch (cmdLine.getCmd()) {
                case CONNECT:
                    controller.joinNetwork();
                    break;
                case DISCONNECT:
                    controller.leaveNetwork();
                    break;
                default:
                    controller.sendMove(cmdLine.getUserInput(), new ConsoleOutput());
            }
        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMgr.println((String) msg);
            outMgr.print(PROMPT);
        }
    }
}
