package server.controller;

import common.Catalog;
import common.FileDTO;
import org.mindrot.jbcrypt.BCrypt;
import server.integration.FileDAO;
import server.integration.UserDAO;
import server.model.File;
import server.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Controller extends UnicastRemoteObject implements Catalog {
    private final UserDAO userDAO;
    private final FileDAO fileDAO;
    private User user = null;

    public Controller() throws RemoteException {
        super();
        this.userDAO = new UserDAO();
        this.fileDAO = new FileDAO();
    }

    @Override
    public void registerUser(String username, String password) {
        if (userDAO.findUserByUsername(username) != null) {
            userDAO.storeUser(new User(username, password));
        }
    }

    @Override
    public void unregisterUser(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            userDAO.destroyUser(user);
        }
    }

    @Override
    public void loginUser(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            this.user = user;
        }
    }

    @Override
    public void logoutUser() {
        if (this.user != null) this.user = null;
    }

    @Override
    public List<? extends FileDTO> findAllFiles() {
        return fileDAO.findAllFiles(this.user);
    }

    @Override
    public void storeFile(String name, boolean privateAccess) {
        fileDAO.storeFile(new File(this.user, name, privateAccess));
    }
}
