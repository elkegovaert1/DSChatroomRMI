import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ClientInterface extends Remote{
	
	public String getName() throws RemoteException;
	public void receiveMessage(String s) throws RemoteException;
	
	

}
