package server.controller;

import common.Catalog;
import common.FileDTO;
import common.UserDTO;
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

    public Controller() throws RemoteException {
        super();
        this.userDAO = new UserDAO();
        this.fileDAO = new FileDAO();
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException {
        if (userDAO.findUserByUsername(username) == null) {
            userDAO.storeUser(new User(username, password));
        } else {
            throw new RemoteException("The username '" + username + "' is already taken!");
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
    public User loginUser(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public List<? extends FileDTO> findAllFiles() throws RemoteException {
        return findAllFiles(null);
    }

    @Override
    public List<? extends FileDTO> findAllFiles(UserDTO owner) {
        User user = owner == null ? null : userDAO.findUserByUsername(owner.getUsername());
        return fileDAO.findAllFiles(user);
    }

    @Override
    public void storeFile(UserDTO owner, String name, boolean privateAccess) {
        fileDAO.storeFile(new File(userDAO.findUserByUsername(owner.getUsername()), name, privateAccess));
    }
}
