package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {
    void outputMessage(String message) throws RemoteException;
}
