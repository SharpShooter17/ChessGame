package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import chessEngine.Board;
import chessEngine.Color;
import chessEngine.Logic;
import chessEngine.NoFigureOnPosition;
import chessEngine.PositionNotValidException;
import common.GameRoomTable;
import common.Move;
import common.Request;
import common.Space;

public class Client {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private int myId;
	private Color color;

	private String hostName;
	private int port;

	private Queue<Request> listRequest;

	public Client( InetAddress inetAddress, int port ) {
		this.hostName = inetAddress.getHostName();
		this.port = port;

		this.listRequest = new ArrayDeque<Request>();

		try {
			this.socket = new Socket(this.hostName, this.port);
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream( this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized Request getrequest(){
		return listRequest.poll();
	}

	@SuppressWarnings("unchecked")
	private void handleTheRequest(Request request){
		System.out.println("Request");
		switch (request.getCodeRequest()){
		case 500:	//lobby list
			SceneController.getSceneController().getLobbyViewController().addLobby(requestGetList(request));
			//addRequest(new Request(405, null));
			break;
		case 501:	//room list
			SceneController.getSceneController().getRoomViewController().addRooms(requestGetList(request));
			break;
		case 203: //refresh room list
			break;
		case 303: //game state
			break;
		case 400:	//go to lobby - LobbyView
			SceneController.getSceneController().setLobbyScene();
			break;
		case 401:	///go to room room view
			SceneController.getSceneController().setRoomScene();
			sendRequest(new Request(200, null));
			break;
		case 402:	//exit from lobby - go to lobby View
			SceneController.getSceneController().setLobbyScene();
			break;
		case 404:	//go to game room
			SceneController.getSceneController().setGameRoomScene();
			break;
		case 405:	//game start
			SceneController.getSceneController().setGameScene();
			SceneController.getSceneController().getGameViewController().setBoard((Board) request.getData());
			SceneController.getSceneController().getGameViewController().setColor(this.color);
			SceneController.getSceneController().getGameViewController().draw();
			break;
		case 407:
			chessEngine.Color color = (chessEngine.Color)request.getData();
			String res = null;
			if ( color == null ){
				res = new String("STALEMATE");
			} else if (color == Color.Black) {
				res = new String("BLACK CHESS WIN");
			} else {
				res = new String("WHITE CHESS WIN");
			}
			SceneController.getSceneController().getGameViewController().modal(res);
			break;
		case 502:
			GameRoomViewController c = SceneController.getSceneController().getGameRoomViewControler();
			c.clearWhiteTable();
			c.clearBlackTable();
			for (GameRoomTable row : (List<GameRoomTable>) request.getData()) {
				if (row.getId() == this.myId){
					c.setText(row.getReady());
					this.color = (row.getLeftSide() ? Color.White : Color.Black);
				}

				if ( row.getLeftSide() ){
					c.addToWhiteTable(new GameRoomTableView(row.getName(), row.getReady()));
				} else {
					c.addToBlackTable(new GameRoomTableView(row.getName(), row.getReady()));
				}
			}
			break;
		case 503:
			this.myId = (int) request.getData();
			break;
		case 504:
			Move move = (Move) request.getData();
			try {
				SceneController.getSceneController().getGameViewController().getBoard().move(move.from, move.to);
				SceneController.getSceneController().getGameViewController().draw();
				System.out.println("Move - FROM: " + move.from.getX() + move.from.getY() + " TO: " + move.to.getX() + move.to.getY() );
			} catch (PositionNotValidException e) {
				e.printStackTrace();
			} catch (NoFigureOnPosition e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private List<SpaceTableView> requestGetList(Request request){
		@SuppressWarnings("unchecked")
		List<Space> obj = (List<Space>) request.getData();
		List<SpaceTableView> list = new ArrayList<SpaceTableView>();
		for (Space space : obj) {
			list.add(new SpaceTableView(space.name, space.players, space.id));
		}
		return list;
	}

	private synchronized void addRequest(Request request){
		listRequest.add(request);
	}

	private void readRequest() throws ClassNotFoundException, IOException{
		if (this.socket.getInputStream().available() > 0) {
			Request request = (Request) this.in.readObject();
			System.out.println("Client get code: " + request.getCodeRequest() );
			addRequest(request);
		}
	}

	public void closeConnection(){
		this.sendRequest(new Request(600, null));
		this.close();
	}

	public void sendRequest(Request request){
		System.out.println("Send request. Code: " + request.getCodeRequest());
		try {
			this.out.reset();
			this.out.writeObject(request);
			this.out.flush();
		} catch (IOException e) {
			this.addRequest(request);
			e.printStackTrace();
		}
	}

	public void run(){
		try {
			readRequest();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if ( !this.listRequest.isEmpty() ){
			this.handleTheRequest(this.getrequest());
		}
	}

	private void close(){
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Color getColor() {
		return color;
	}

	public int getMyId(){
		return this.myId;
	}
}
