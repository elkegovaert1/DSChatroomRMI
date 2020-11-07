import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientScreen extends Application {
	public ServerInterface si;
	public ClientImpl ci;

	private void startClient() throws RemoteException {
		ClientInterface ci = null;

		try {
			if (System.getSecurityManager() == null) {
				System.setProperty("java.security.policy", "file:\\C:\\Users\\elkeg\\IdeaProjects\\DSChatroomRMIClient\\src\\security.policy");
				RMISecurityManager securityManager = new RMISecurityManager();
	            System.setSecurityManager(securityManager);
	        }

			Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);
		    ServerInterface si =  (ServerInterface) myRegistry.lookup("ChatService");
		    
			Scanner scanner=new Scanner(System.in);
		    System.out.println("[System] Client Messenger is running");
		    System.out.println("Enter a username to login and press Enter:");
		    String username = scanner.nextLine();
		    ci = new ClientImpl(username,si);
		    si.newClient(ci);
		    si.sendToAll("Just Connected",ci);	
		      for(;;){  	
		    	  String aa = scanner.nextLine();
				  si.sendToAll(aa,ci);				    		  				  				    	  
	    	  }
		}catch (Exception e) {
			//ci.disconnected();
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws RemoteException {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Chat Client");
		primaryStage.setScene(makeInitScene(primaryStage));
		primaryStage.show();
	}

	public Scene makeInitScene(Stage primaryStage) {
		GridPane rootPane = new GridPane();
		rootPane.setPadding(new Insets(20));
		rootPane.setVgap(10);
		rootPane.setHgap(10);
		rootPane.setAlignment(Pos.CENTER);

		TextField nameField = new TextField();

		Label nameLabel = new Label("Name");
		Label errorLabel = new Label();

		Button submitClientInfoButton = new Button("Done");

		submitClientInfoButton.setOnAction(Event -> {
			try {
				if (System.getSecurityManager() == null) {
					System.setProperty("java.security.policy", "file:\\C:\\Users\\elkeg\\IdeaProjects\\DSChatroomRMIClient\\src\\security.policy");
					RMISecurityManager securityManager = new RMISecurityManager();
					System.setSecurityManager(securityManager);
				}

				Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);
				si =  (ServerInterface) myRegistry.lookup("ChatService");

				System.out.println("[System] Client Messenger is running");

				String username = nameField.getText();
				ci = new ClientImpl(username, si);
				si.newClient(ci);
				si.sendToAll("Just Connected",ci);

				/* Change the scene of the primaryStage */
				primaryStage.close();
				primaryStage.setScene(makeChatUI(ci, si));
				primaryStage.setTitle(ci.getName());
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

		});

		rootPane.add(nameField, 0, 0);
		rootPane.add(nameLabel, 1, 0);
		rootPane.add(submitClientInfoButton, 0, 3, 2, 1);
		rootPane.add(errorLabel, 0, 4);

		return new Scene(rootPane, 400, 400);
	}

	public Scene makeChatUI(ClientImpl client, ServerInterface server) {
		GridPane rootPane = new GridPane();
		rootPane.setPadding(new Insets(20));
		rootPane.setAlignment(Pos.CENTER);
		rootPane.setHgap(10);
		rootPane.setVgap(10);


		ListView<String> chatListView = new ListView<String>();
		chatListView.setItems(client.chatLog);

		//setupPriveListView(rootPane);

		TextField chatTextField = new TextField();
		chatTextField.setOnAction(event -> {
			try {
				server.sendToAll(chatTextField.getText(), client);
				chatTextField.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		rootPane.add(chatListView, 0, 0);
		rootPane.add(chatTextField, 0, 1);

		return new Scene(rootPane, 600, 400);

	}

	@Override
	public void stop() throws RemoteException {
		si.disconnectClient(ci);
		System.exit(0);
	}

}
