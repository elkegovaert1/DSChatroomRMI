import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javafx.collections.ObservableList;

public interface ServerInterface extends Remote{
	
	public boolean newClient(ClientInterface ci) throws RemoteException;
	
	public void sendToAll(String s, ClientInterface from) throws RemoteException;
	
	public ClientInterface getClient(String s) throws RemoteException;

	public void disconnectClient(ClientInterface ci) throws RemoteException;
	
	public ObservableList<String> getClientNames() throws RemoteException;
	
	public void sendToOne(String s, ClientInterface from, String to) throws RemoteException;

}