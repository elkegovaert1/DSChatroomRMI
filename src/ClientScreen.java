import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientScreen {
	private void startClient() {
		try {
			if (System.getSecurityManager() == null) {
				System.setProperty("java.security.policy", "file:\\C:\\Users\\elkeg\\IdeaProjects\\DSChatroomRMIClient\\src\\security.policy");
				RMISecurityManager securityManager = new RMISecurityManager();
	            System.setSecurityManager(securityManager);
	        }

			Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);
		    ServerInterface si =  (ServerInterface) myRegistry.lookup("ChatService");

			//System.setSecurityManager(new RMISecurityManager());
		    //ServerInterface si = (ServerInterface)Naming.lookup("rmi://localhost/ABCD");
		    
			Scanner scanner=new Scanner(System.in);
		    System.out.println("[System] Client Messenger is running");
		    System.out.println("Enter a username to login and press Enter:");
		    String username = scanner.nextLine();
		    ClientInterface ci = new ClientImpl(username,si);
		    si.newClient(ci);
		    si.sendToAll("Just Connected",ci);	
		      for(;;){  	
		    	  String aa = scanner.nextLine();
				  si.sendToAll(aa,ci);				    		  				  				    	  
	    	  }
		}catch (Exception e) {
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ClientScreen cs = new ClientScreen();
		cs.startClient();
		// TODO Auto-generated method stub

	}

}
