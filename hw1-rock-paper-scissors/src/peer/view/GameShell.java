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
            CmdLine cmdLine = new CmdLine(console.nextLine());
            switch (cmdLine.getCmd()) {
                case CONNECT:
                    controller.joinNetwork();
                    break;
                case DISCONNECT:
                    controller.leaveNetwork();
                    break;
                //case MAKE_MOVE:
                   // controller.sendMove(cmdLine.getUserInput(), new ConsoleOutput());
                case NO_COMMAND:
                    outMgr.print(PROMPT);
                default:
                    outMgr.print("Pew");
            }
        }
    }

    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMgr.println((String) msg);
            outMgr.print(PROMPT);
        }
    }
}
