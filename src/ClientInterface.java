import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javafx.collections.ObservableList;



public interface ClientInterface extends Remote{
	
	public String getName() throws RemoteException;
	public void receiveMessage(String s) throws RemoteException;
	public void disconnected() throws RemoteException;
	public void generatePriveBerichten(List<String> clientNames) throws RemoteException;
	public void disconnectClient(String s) throws RemoteException;
	public void connectClient(String s) throws RemoteException;
	public void receivePrivateMessage(String s, String sender) throws RemoteException;
	public void addPrivateMessage(String s, String receiver) throws RemoteException;
	
	

}
