package server;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ServerConfiguration {
	private static ServerConfiguration instance = null;

	private int port;

	private ServerConfiguration(){
		this.load();
	}

	static ServerConfiguration getInstance(){
		if (!(instance instanceof ServerConfiguration)){
			instance = new ServerConfiguration();
		}
		return ServerConfiguration.instance;
	}

	private void load(){

		try {
			File fXmlFile = new File(this.getClass().getResource("data/config.xml").getFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			this.port = Integer.parseInt( doc.getElementsByTagName("port").item(0).getTextContent() );

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	int getPort(){
		return this.port;
	}
}
