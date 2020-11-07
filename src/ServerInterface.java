import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote{
	
	public boolean newClient(ClientInterface ci) throws RemoteException;
	
	public void sendToAll(String s, ClientInterface from) throws RemoteException;
	
	public ClientInterface getClient(String s) throws RemoteException;

	public void disconnectClient(ClientInterface ci) throws RemoteException;

}