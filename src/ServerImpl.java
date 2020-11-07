import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	
	private ObservableList<ClientInterface> clients;
	public static ObservableList<String> clientNames;

	public ServerImpl() throws RemoteException{
		clients = FXCollections.observableArrayList();
		clientNames = FXCollections.observableArrayList();
	}

	@Override
	public boolean newClient(ClientInterface ci) throws RemoteException{
		clients.add(ci);
		clientNames.add(ci.getName());
		ci.receiveMessage("[Server] Welcome Client "+ci.getName());
		return true;
	}

	@Override
	public void sendToAll(String s, ClientInterface from) throws RemoteException{
		for(ClientInterface ci : clients) {
			try {
				ci.receiveMessage("\n["+from.getName()+"] "+ s);
			} catch(Exception e){e.printStackTrace();}
		}
	}

	@Override
	public ClientInterface getClient(String username) throws RemoteException{
		for(ClientInterface ci : clients) {
			if(ci.getName().equals(username)) {
				return ci;
			}
		}
		return null;
	}

	@Override
	public void disconnectClient(ClientInterface ci) throws RemoteException {
		clientNames.removeAll(ci.getName());
		clients.remove(ci);
	}

}
