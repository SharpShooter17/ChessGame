package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Controller {
	private Client client;

	public Controller() throws UnknownHostException {
		client = new Client(InetAddress.getLocalHost(), 62000);
	}
	public Client getClient() {
		return client;
	}
}
