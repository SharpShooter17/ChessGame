package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

public class Controller {
	private Client client;

	public Controller() throws IOException {
		String ip = loadIpAddress();
		client = new Client(InetAddress.getByName(ip), 62000);
	}
	public Client getClient() {
		return client;
	}
	private String loadIpAddress() throws IOException{
		File f = new File(this.getClass().getResource("data/config.txt").getFile());
		BufferedReader in = new BufferedReader( new FileReader(f) );
		String res = in.readLine();
		in.close();

		return res;
	}
}
