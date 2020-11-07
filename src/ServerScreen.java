import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class ServerScreen {
	private void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("ChatService", new ServerImpl());
				 
			System.out.println("[System] Chat Server is ready.");               
		}catch (Exception e) {
			System.out.println("Chat Server failed: " + e);
		}
	}
	
	public static void main(String[] args) {
		ServerScreen sc = new ServerScreen();
		sc.startServer();
	}
}

