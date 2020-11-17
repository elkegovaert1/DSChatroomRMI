import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
	private String name;
	private ServerInterface server;
	public static ObservableList<String> chatLog;
	public ObservableList<priveGesprek> berichten;

	public ClientImpl(String username, ServerInterface si) throws RemoteException{
		name = username;
		server = si;
		chatLog = FXCollections.observableArrayList();
		berichten = FXCollections.observableArrayList();
	}
	@Override
	public String getName() throws RemoteException{
		return name;
	}
	@Override
	public void receiveMessage(String s) throws RemoteException{
		Platform.runLater(() -> {
			chatLog.add(s);
		});
	}

	@Override
	public void disconnected() throws RemoteException {
		server.disconnectClient(this);
	}
	@Override
	public void generatePriveBerichten(List<String> clientNames) throws RemoteException {
		for(String name : clientNames) {
			if(!this.name.equals(name)) {
				priveGesprek pg = new priveGesprek(this, name);
				berichten.add(pg);
			}			
		}		
	}
	@Override
	public void disconnectClient(String s) throws RemoteException{
		for(priveGesprek pg : berichten) {
			if(pg.getPartner().equals(s)) {
				Platform.runLater(() -> berichten.remove(pg));
				break;
			}
		}	
	}
	@Override
	public void connectClient(String s) throws RemoteException {
		priveGesprek pg = new priveGesprek(this, s);
		Platform.runLater(() -> {
			berichten.add(pg);
		});
	}

	@Override
	public void receivePrivateMessage(String s, String sender) throws RemoteException {
		for (int i = 0; i < berichten.size(); i++) {
			priveGesprek pg = berichten.get(i);
			if(pg.getPartner().equals(sender)){
				Platform.runLater(() -> pg.addBericht("[" + sender + "] " + s));
				break;
			}
		}
		// de fout is dat als we een bericht ook zelf moeten ontvangen dat we niet weten in welk gesprek t moet komen
	}

	@Override
	public void addPrivateMessage(String s, String receiver) throws RemoteException{
		for (int i = 0; i < berichten.size(); i++) {
			priveGesprek pg = berichten.get(i);
			if(pg.getPartner().equals(receiver)){
				Platform.runLater(() -> pg.addBericht("[" + name + "] " + s));
				break;
			}
		}
	}
}
