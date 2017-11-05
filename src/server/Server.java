package server;

import java.util.ArrayList;
import java.util.List;

import common.Request;
import common.Space;
import javafx.util.Pair;

public class Server extends Receiver {
	public static final int RefreshAllLobbyList = 0;


	private List<Lobby> lobbyList;
	public Server(){
		super(null);
		this.lobbyList = new ArrayList<Lobby>();
		lobbyList.add(new Lobby("Easy", this));
		lobbyList.add(new Lobby("Medium", this));
		lobbyList.add(new Lobby("Hard", this));

		for (Lobby lobby : lobbyList) {
			lobby.start();
		}
	}

	@Override
	protected void handleTheRequest(Pair<Request, Client> request) {
		Request req = request.getKey();
		Client client = request.getValue();

		switch (req.getCodeRequest()){
		case 100:	//get lobby list
			Request respond = new Request(500, this.requestGetLobbyList());
			client.addRequest(respond);
			client.addRequest(new Request(400, null));
			break;
		case 101:	//enter lobby
			if (requestEnterToLobby(req, client, getLobby(((Space)req.getData()).id))){
				client.addRequest(new Request(401, null));
				super.addTask(Server.RefreshAllLobbyList);
			}
			break;
		default:
			System.out.println("Server - Incoming request code: " + req.getCodeRequest());
			break;
		}
	}
	@Override
	public boolean addClient(Client client) {
		boolean res = super.addClient(client);
		if (res){
			client.addRequest(new Request(503, client.getMyId()));
		}
		return res;
	}

	@Override
	protected void sendRespond() {
	}
	@Override
	protected void loop() {
	}
	public final List<Lobby> getLobbyList(){
		return lobbyList;
	}
	private boolean requestEnterToLobby(Request req, Client client, Lobby lobby){
		if ( lobby.addClient(client) && lobby != null){
			client.setReceiver(lobby);
			this.removeClient(client);
			return true;
		} else {
			return false;
		}
	}
	private List<Space> requestGetLobbyList(){
		List<Space> result = new ArrayList<Space>();

		for (Lobby lobby : this.lobbyList) {
			result.add(new Space( lobby.getLobbyName(), lobby.getMyId(), lobby.countOfClients()));
		}

		return result;
	}
	private Lobby getLobby(int id){
		for (Lobby lobby : lobbyList) {
			if ( lobby.getMyId() == id ){
				return lobby;
			}
		}
		return null;
	}
	@Override
	protected void handleTheTask(Integer task) {
		 if (task == Server.RefreshAllLobbyList ){
			Request request = new Request(500, this.requestGetLobbyList());
			for (Client client : this.getClientList()) {
				client.addRequest(request);
			}
		 }
	}
}
