package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Listener {
	private Server server;
	Listener(){
		server = new Server();
	}

	public void run(){
		server.start();
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(ServerConfiguration.getInstance().getPort());
			while (true) {
				Client client = new Client(server, serverSocket.accept());
				server.addClient(client);
				client.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			server.close();
			System.exit(-1);
		}
		server.close();
	}

	public static void main(String[] args){
		new Listener().run();
	}

}
