package client.view;

import common.Catalog;
import common.FileDTO;
import common.PrettyPrinter;
import common.UserDTO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class CatalogShell implements Runnable {
    private static final String PROMPT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private Catalog catalog;
    private UserDTO user = null;
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
                        outMgr.println(PrettyPrinter.buildSimpleMessage("User '" + parsedLine.getArgument(0) + "' successfully registered!"));
                        break;
                    case UNREGISTER:
                        catalog.unregisterUser(parsedLine.getArgument(0), parsedLine.getArgument(1));
                        this.user = null;
                        outMgr.println(PrettyPrinter.buildSimpleMessage("User '" + parsedLine.getArgument(0) + "' successfully unregistered!"));
                        break;
                    case LOGIN:
                        this.user = catalog.loginUser(parsedLine.getArgument(0), parsedLine.getArgument(1));
                        outMgr.println(PrettyPrinter.buildSimpleMessage("Logged in as '" + user.getUsername() + "'!"));
                        break;
                    case LOGOUT:
                        this.user = null;
                        outMgr.println(PrettyPrinter.buildSimpleMessage("Logout successful!"));
                        break;
                    case STORE_FILE:
                        if (this.user != null) {
                            catalog.storeFile(this.user, parsedLine.getArgument(0), Boolean.parseBoolean(parsedLine.getArgument(1)));
                            outMgr.println(PrettyPrinter.buildSimpleMessage("File uploaded successfully!"));
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to upload a file!"));
                        }
                        break;
                    case LIST_FILES:
                        List<? extends FileDTO> list = user != null ? catalog.findAllFiles(user) : catalog.findAllFiles();
                        outMgr.print(PrettyPrinter.buildSimpleMessage("The catalog contains the following files:"));
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