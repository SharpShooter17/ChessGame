package server;

import java.util.ArrayList;
import java.util.List;

import common.Request;
import common.Space;
import javafx.util.Pair;

public class Lobby extends Receiver {
	public static final int RefreshAllRoomList = 0;

	private String name;
	private List<Room> roomList;

	Lobby(String name, Receiver parent){
		super(parent);
		this.name = name;
		this.roomList = new ArrayList<Room>();

		this.newRoom(new String("YOLO"));
		this.newRoom(new String("YOYO"));
		this.newRoom(new String("XOXO"));
	}

	public String getLobbyName(){
		return this.name;
	}

	@Override
	protected void sendRespond() {
	}
	@Override
	protected void loop() {
	}
	public List<Room> getRoomList() {
		return roomList;
	}
	@Override
	protected void handleTheRequest(Pair<Request, Client> request) {
		Request req = request.getKey();
		Client client = request.getValue();

		switch(req.getCodeRequest()){
		case 200:	//get room list
			Request respond = new Request(501, this.requestGetRoomList());
			client.addRequest(respond);
			break;
		case 201:	//New room
			Room room = newRoom((String) req.getData());
			if (room != null) {
				this.enterToRoom(room, client);
			}
			break;
		case 202: // enter to room
			Room r = this.getRoom((Integer) req.getData());
			if (r != null)
				this.enterToRoom(r, client);
			break;
		case 203:
			break;
		case 204:	//back to selecting server
			backToSever(req, client);
			break;
		default:
			System.out.println("Lobby - Incoming request code: " + req.getCodeRequest());
			break;
		}
	}

	private void backToSever(Request req, Client client){
		if (this.getParent().addClient(client) ){
			client.setReceiver(this.getParent());
			this.removeClient(client);
			client.addRequest(new Request(402, null));
			((Server) this.getParent()).addTask(Server.RefreshAllLobbyList);
		}
	}

	private List<Space> requestGetRoomList(){
		List<Space> result = new ArrayList<Space>();

		for (Room room : this.roomList) {
			result.add(new Space( room.getRoomName(), room.getMyId(), room.getClientList().size()));
		}

		return result;
	}

	private Room getRoom(int id){
		for (Room room : roomList) {
			if (room.getMyId() == id){
				return room;
			}
		}
		return null;
	}

	private Room newRoom(String name){
		Room room = new Room( name, this );
		roomList.add(room);
		room.start();
		return room;
	}

	@Override
	protected void handleTheTask(Integer task) {
		if (task == Lobby.RefreshAllRoomList){
			Request request = new Request(501, this.requestGetRoomList());
			for (Client client : super.getClientList()) {
				client.addRequest(request);
			}
		}
	}

	private boolean enterToRoom(Room r, Client client){
		if ( r.addClient(client) ){
			this.removeClient(client);
			client.setReceiver(r);
			client.addRequest(new Request(404 ,null));
			super.addTask(Lobby.RefreshAllRoomList);
			return true;
		} else{
			return false;
		}
	}

	public int countOfClients(){
		int count = this.getClientList().size();
		for (Room room : roomList) {
			count += room.getClientList().size();
		}
		return count;
	}

}
