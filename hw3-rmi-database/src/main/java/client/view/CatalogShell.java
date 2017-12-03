package client.view;

import client.FileUtility;
import common.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class CatalogShell implements Runnable {
    private static final String PROMPT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private NotificationServer outputHandler;
    private Catalog catalog;
    private UserDTO user = null;
    private boolean running = false;

    public void start(Catalog catalog) {
        this.catalog = catalog;

        try {
            this.outputHandler = new NotificationServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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
                            byte[] data = FileUtility.readFile(parsedLine.getArgument(0));
                            catalog.storeFile(
                                    this.user, parsedLine.getArgument(0), data,
                                    Boolean.parseBoolean(parsedLine.getArgument(1)),
                                    Boolean.parseBoolean(parsedLine.getArgument(2)),
                                    Boolean.parseBoolean(parsedLine.getArgument(3))
                            );
                            outMgr.println(PrettyPrinter.buildSimpleMessage("File uploaded successfully!"));
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to upload a file!"));
                        }
                        break;
                    case LIST_FILES:
                        if (user == null) {
                            outMgr.print(PrettyPrinter.buildSimpleMessage("Please login to interact with the file catalog."));
                            break;
                        }

                        List<? extends FileDTO> list = user != null ? catalog.findAllFiles(user) : catalog.findAllFiles();
                        outMgr.print(PrettyPrinter.buildSimpleMessage("The catalog contains the following files:"));
                        outMgr.println("NAME (SIZE) - PRIVATE|WRITE|READ");
                        for (FileDTO file : list) {
                            outMgr.println(file.getName() + " (" + file.getDimension() + "bytes) - " + file.hasPrivateAccess() +
                                    "|" + file.hasWritePermission() + "|" + file.hasReadPermission());
                        }
                        break;
                    case GET_FILE:
                        if (this.user != null) {
                            FileUtility.writeFile(parsedLine.getArgument(0), catalog.getFile(user, parsedLine.getArgument(0)));
                            outMgr.println(PrettyPrinter.buildSimpleMessage("File downloaded successfully!"));
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to download a file!"));
                        }
                        break;
                    case DELETE_FILE:
                        if (this.user != null) {
                            catalog.deleteFile(user, parsedLine.getArgument(0));
                            outMgr.println(PrettyPrinter.buildSimpleMessage("File deleted successfully!"));
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to delete a file!"));
                        }
                        break;
                    case UPDATE_FILE:
                        if (this.user != null) {
                            byte[] data = FileUtility.readFile(parsedLine.getArgument(0));
                            catalog.updateFile(
                                    this.user, parsedLine.getArgument(0), data,
                                    Boolean.parseBoolean(parsedLine.getArgument(1)),
                                    Boolean.parseBoolean(parsedLine.getArgument(2)),
                                    Boolean.parseBoolean(parsedLine.getArgument(3))
                            );
                            outMgr.println(PrettyPrinter.buildSimpleMessage("File updated successfully!"));
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to update a file!"));
                        }
                        break;
                    case NOTIFY:
                        if (this.user != null) {
                            catalog.notify(this.user, parsedLine.getArgument(0), this.outputHandler);
                        } else {
                            outMgr.println(PrettyPrinter.buildSimpleMessage("You need to be logged in to receive notifications!"));
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
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
                outMgr.print(PrettyPrinter.buildCommandErrorMessage(e.getMessage()));
                outMgr.print(PROMPT);
            }
        }
    }

    private class NotificationServer extends UnicastRemoteObject implements ClientRemote {
        public NotificationServer() throws RemoteException {
        }

        @Override
        public void outputMessage(String message) throws RemoteException {
            outMgr.println(PrettyPrinter.buildSimpleMessage(message));
            outMgr.print(PROMPT);
        }
    }

}