import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Platform;
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
		Platform.runLater(() -> {
			try {
				for(ClientInterface client : clients) {
					client.connectClient(ci.getName());
				}
				clients.add(ci);
				clientNames.add(ci.getName());
				ci.receiveMessage("[Server] Welcome Client "+ci.getName());
				List<String> list = new ArrayList<>();
				for(String s : clientNames) {
					list.add(s);
				}
				ci.generatePriveBerichten(list);
				

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
		return true;
	}

	@Override
	public void sendToAll(String s, ClientInterface from) throws RemoteException{
		for (int i = 0; i < clients.size(); i++) {
			try {
				clients.get(i).receiveMessage("["+from.getName()+"] "+ s);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		/*for(ClientInterface ci : clients) {
			try {
				ci.receiveMessage("["+from.getName()+"] "+ s);
			} catch(Exception e){e.printStackTrace();}
		}*/
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
		Platform.runLater(() ->  {
			try {
				clientNames.removeAll(ci.getName());
				clients.remove(ci);
				for(ClientInterface client : clients) {
					client.disconnectClient(ci.getName());
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public ObservableList<String> getClientNames() throws RemoteException {
		return clientNames;
	}

	@Override
	public void sendToOne(String s, ClientInterface from, String to) throws RemoteException {
		System.out.println("Sending to one... " + to);
		for (int i = 0; i < clients.size(); i++) {
			if(to.equals(clients.get(i).getName())) {
				// naar persoon
				clients.get(i).receivePrivateMessage(s, from.getName());
				// naar zelf
				from.addPrivateMessage(s, to);
				//from.receivePrivateMessage(s, from.getName());
				break;
			}
		}
		/*for(ClientInterface client : clients) {
			if(to.equals(client.getName())) {
				client.receivePrivateMessage(s, from.getName());
				break;
			}
		}*/

		//from.receivePrivateMessage(s, from.getName());
	}

}
