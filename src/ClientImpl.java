import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
	private String name;
	private ServerInterface server;
	
	public ClientImpl(String username, ServerInterface si) throws RemoteException{
		name = username;
		server = si;
	}
	@Override
	public String getName() throws RemoteException{
		return name;
	}
	@Override
	public void receiveMessage(String s) throws RemoteException{
		System.out.println(s);
	}

}
