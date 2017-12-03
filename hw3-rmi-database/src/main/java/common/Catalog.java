package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Catalog extends Remote {

    public void registerUser(String username, String password) throws RemoteException;

    public void unregisterUser(String username, String password) throws RemoteException;

    public UserDTO loginUser(String username, String password) throws RemoteException;

    public List<? extends FileDTO> findAllFiles(UserDTO owner) throws RemoteException;

    public List<? extends FileDTO> findAllFiles() throws RemoteException;

    public void storeFile(UserDTO owner, String name, boolean privateAccess) throws RemoteException;
}
