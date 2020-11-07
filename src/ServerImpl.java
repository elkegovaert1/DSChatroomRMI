import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	
	private ObservableList<ClientInterface> clients;
	
	public ServerImpl() throws RemoteException{
		clients = FXCollections.observableArrayList();
	}
	public boolean newClient(ClientInterface ci) throws RemoteException{
		clients.add(ci);
		ci.receiveMessage("[Server] Welcome Client "+ci.getName());
		return true;
	}
	public void sendToAll(String s, ClientInterface from) throws RemoteException{
		for(ClientInterface ci : clients) {
			try {
				ci.receiveMessage("\n["+from.getName()+"] "+ s);
			} catch(Exception e){e.printStackTrace();}
		}
	}
	public ClientInterface getClient(String username) throws RemoteException{
		for(ClientInterface ci : clients) {
			if(ci.getName().equals(username)) {
				return ci;
			}
		}
		return null;
	}
	

}
