package common;

import java.rmi.Remote;
import java.util.List;

public interface Catalog extends Remote {

    public void registerUser(String username, String password);

    public void unregisterUser(String username, String password);

    public void loginUser(String username, String password);

    public void logoutUser();

    public List<? extends FileDTO> findAllFiles();

    public void storeFile(String name, boolean privateAccess);
}
