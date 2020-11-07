import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;

public class ServerScreen extends Application {
	private void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("ChatService", new ServerImpl());
				 
		}catch (Exception e) {
			System.out.println("Chat Server failed: " + e);
		}
	}
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Chat Server");
		primaryStage.setScene(makeUI(primaryStage));
		primaryStage.show();

	}

	public Scene makeUI(Stage primaryStage) throws IOException {
		GridPane rootPane = new GridPane();
		rootPane.setAlignment(Pos.CENTER);
		rootPane.setPadding(new Insets(20));
		rootPane.setVgap(10);
		rootPane.setHgap(10);

		Registry registry = LocateRegistry.createRegistry(1099);
		ServerImpl si = new ServerImpl();
		registry.rebind("ChatService", si);
		System.out.println("[System] Chat Server is ready.");

		Label clientLabel = new Label("Clients Connected");
		ListView<String> clientView = new ListView<>();
		ObservableList<String> clientList = si.clientNames;
		clientView.setItems(clientList);

		rootPane.add(clientLabel, 0, 0);
		rootPane.add(clientView, 0, 1);

		primaryStage.show();

		return new Scene(rootPane, 400, 300);

	}

	@Override
	public void stop() throws RemoteException {
		System.exit(0);
	}
}

