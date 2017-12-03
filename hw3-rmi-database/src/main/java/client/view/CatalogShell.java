package client.view;

import common.Catalog;
import common.FileDTO;
import common.PrettyPrinter;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class CatalogShell implements Runnable {
    private static final String PROMPT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private Catalog catalog;
    private boolean running = false;

    public void start(Catalog catalog) {
        this.catalog = catalog;

        if (running) return;
        running = true;

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
                    case REGISTER:
                        catalog.registerUser(parsedLine.getArgument(0), parsedLine.getArgument(1));
                        break;
                    case UNREGISTER:
                        catalog.unregisterUser(parsedLine.getArgument(0), parsedLine.getArgument(1));
                        break;
                    case LOGIN:
                        catalog.loginUser(parsedLine.getArgument(0), parsedLine.getArgument(1));
                        break;
                    case LOGOUT:
                        catalog.logoutUser();
                        break;
                    case STORE_FILE:
                        catalog.storeFile(parsedLine.getArgument(0), Boolean.parseBoolean(parsedLine.getArgument(1)));
                        break;
                    case LIST_FILES:
                        List<? extends FileDTO> list = catalog.findAllFiles();
                        for (FileDTO file : list) {
                            outMgr.println(file.getName());
                        }
                        break;
                    case HELP:
                        outMgr.print(PrettyPrinter.buildHelpMessage());
                        break;
                    case QUIT:
                        running = false;
                        break;
                    case NO_COMMAND:
                        break;
                }
                outMgr.print(PROMPT);
            } catch (IllegalArgumentException | RemoteException e) {
                outMgr.print(PrettyPrinter.buildCommandErrorMessage(e.getMessage()));
                outMgr.print(PROMPT);
            }
        }
    }

}