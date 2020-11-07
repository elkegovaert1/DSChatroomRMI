import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
	private String name;
	private ServerInterface server;
	public static ObservableList<String> chatLog;

	public ClientImpl(String username, ServerInterface si) throws RemoteException{
		name = username;
		server = si;
		chatLog = FXCollections.observableArrayList();
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

}
