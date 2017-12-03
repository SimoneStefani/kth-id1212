package server.controller;

import common.Catalog;
import common.FileDTO;
import common.UserDTO;
import org.mindrot.jbcrypt.BCrypt;
import server.integration.FileDAO;
import server.integration.UserDAO;
import server.model.File;
import server.model.FileManager;
import server.model.User;

import java.io.IOException;
import java.nio.file.Path;
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
    public UserDTO loginUser(String username, String password) throws RemoteException {
        User user = userDAO.findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new RemoteException("Invalid credentials!");
        }
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
    public void storeFile(UserDTO owner, String name, byte[] content, boolean privateAccess, boolean publicWrite, boolean publicRead) throws IOException {
        if (fileDAO.findFileByName(name) == null) {
            fileDAO.storeFile(new File(userDAO.findUserByUsername(owner.getUsername()), name, privateAccess, publicWrite, publicRead, content.length));
            FileManager.persistFile(name, content);
        } else {
            throw new RemoteException("The file '" + name + "' already exists!");
        }

    }

    @Override
    public byte[] getFile(UserDTO userDTO, String name) throws IOException {
        User user = userDAO.findUserByUsername(userDTO.getUsername());
        File file = fileDAO.findFileByName(name);

        if (file == null) {
            throw new RemoteException("The file '" + name + "' does not exists!");
        } else if (file.getOwner().getId() == user.getId() || (!file.hasPrivateAccess() && file.hasReadPermission())) {
            return FileManager.getFile(name);
        } else {
            throw new RemoteException("You don't have the permission to download this file");
        }
    }

    @Override
    public void updateFile(UserDTO owner, String name, byte[] content, boolean privateAccess, boolean publicWrite, boolean publicRead) throws IOException {
        User user = userDAO.findUserByUsername(owner.getUsername());
        File file = fileDAO.findFileByName(name);

        if (file == null) {
            throw new RemoteException("The file '" + name + "' does not exists!");
        } else if (file.getOwner().getId() == user.getId() || (!file.hasPrivateAccess() && file.hasWritePermission())) {
            fileDAO.updateFile(new File(userDAO.findUserByUsername(owner.getUsername()), name, privateAccess, publicWrite, publicRead, content.length));
            FileManager.persistFile(name, content);
        } else {
            throw new RemoteException("You don't have the permission to update this file");
        }
    }

    @Override
    public void deleteFile(UserDTO userDTO, String name) throws IOException {
        User user = userDAO.findUserByUsername(userDTO.getUsername());
        File file = fileDAO.findFileByName(name);

        if (file == null) {
            throw new RemoteException("The file '" + name + "' does not exists!");
        } else if (file.getOwner().getId() == user.getId() || (!file.hasPrivateAccess() && file.hasWritePermission())) {
            fileDAO.destroyFile(file);
            FileManager.deleteFile(name);
        } else {
            throw new RemoteException("You don't have the permission to delete this file");
        }
    }
}
