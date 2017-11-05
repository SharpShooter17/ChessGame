package server;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import chessEngine.Board;
import chessEngine.Color;
import chessEngine.Logic;
import chessEngine.NoFigureOnPosition;
import chessEngine.NoKingOnTheBoardException;
import chessEngine.Position;
import chessEngine.PositionNotValidException;
import common.GameRoomTable;
import common.Move;
import common.Request;
import common.RequestCodes;
import javafx.util.Pair;

public class Room extends Receiver {
	private static final int RefreshTables = 0;
	private static final int SendMove = 1;
	private static final int SendResult = 2;

	private static final int SizeOfRoom = 2;

	private final String name;

	private Logic logic;

	private Queue<Move> moveTosend;
	private Color win;

	@Override
	public boolean addClient(Client client){
		if ( this.getClientList().size() > Room.SizeOfRoom - 1 ) {
			return false;
		}

		int left = 0;
		int right = 0;

		for (GameRoomTable table : gameRoomTable) {
			if (table.getLeftSide()){
				left++;
			} else {
				right++;
			}
		}
		GameRoomTable roomTable = new GameRoomTable("Guest_" + client.getId() + 100*client.getMyId(), false, left <= right ? true : false, client.getMyId());

		this.gameRoomTable.add(roomTable);
		this.addTask(Room.RefreshTables);

		return super.addClient(client);
	}

	private List<GameRoomTable> gameRoomTable;

	public Room(String name, Receiver parent) {
		super(parent);
		this.name = name;
		this.gameRoomTable = new ArrayList<GameRoomTable>();
		this.moveTosend = new ArrayDeque<>();
	}

	@Override
	protected void sendRespond() {

	}

	@Override
	protected void loop() {
	}

	@Override
	protected void handleTheRequest(Pair<Request, Client> request) {
		Request req = request.getKey();
		Client client = request.getValue();
		switch (req.getCodeRequest()){
		case 305:
			this.gameRoomTable.remove(this.getClientGameRoomTable(client.getMyId()));
			this.backToLobby(req, client);
			this.addTask(Room.RefreshTables);
			break;
		case 300: //ready to play
			this.getClientGameRoomTable(client.getMyId()).setReady(true);
			this.addTask(Room.RefreshTables);
			break;
		case 302:
			int id = client.getMyId();
			try {
				Color c = this.getColor(id);
				this.win = c == Color.White ? Color.Black : Color.White;
				this.addTask(Room.SendResult);
			} catch (NoIdInListException e) {
				e.printStackTrace();
			}
			break;
		case 301: //not ready to play
			this.getClientGameRoomTable(client.getMyId()).setReady(false);
			this.addTask(Room.RefreshTables);
			break;
		case 304: //move
			try {
				if ( this.getColor(client.getMyId()) != this.logic.getActualPlayer() ) {
					return;
				}
			} catch (NoIdInListException e1) {
				return;
			}
			Move move = (Move) req.getData();
			try {
				if ( this.logic.makeMove(move.from, move.to) ){
					this.moveTosend.add(move);
					this.addTask(Room.SendMove);
					this.logic.changePlayer();
					this.state();
					System.out.println(this.logic.getGameState().toString());
				}
			} catch (PositionNotValidException e) {
				e.printStackTrace();
			} catch (NoKingOnTheBoardException e) {
				e.printStackTrace();
			} catch (NoFigureOnPosition e) {
				e.printStackTrace();
			}
			break;
		case 306:
			this.addTask(Room.RefreshTables);
			break;
		case 307: //short castling
			try {
				if ( this.getColor(client.getMyId()) == this.logic.getActualPlayer() ){
					if (this.logic.makeShortCastling(this.logic.getActualPlayer())){
						int y = this.getColor(client.getMyId()) == Color.White ? 0 : 7;
						this.moveTosend.add( new Move( new Position(3, y), new Position(1, y) ) );
						this.addTask(Room.SendMove);
						this.moveTosend.add( new Move( new Position(0, y), new Position(2, y) ) );
						this.addTask(Room.SendMove);
						this.logic.changePlayer();
						this.state();
						System.out.println(this.logic.getGameState().toString());
					} else {
						System.out.println("False");
					}
				}
			} catch (PositionNotValidException | NoFigureOnPosition | NoKingOnTheBoardException | NoIdInListException e) {
				e.printStackTrace();
			}
			break;
		case 308: //long castling
			try {
				if ( this.getColor(client.getMyId()) == this.logic.getActualPlayer() ){
					if (this.logic.makeLongCastling(this.logic.getActualPlayer())){
						int y = this.getColor(client.getMyId()) == Color.White ? 0 : 7;
						this.moveTosend.add( new Move( new Position(3, y), new Position(5, y) ) );
						this.addTask(Room.SendMove);
						this.moveTosend.add( new Move( new Position(7, y), new Position(4, y) ) );
						this.addTask(Room.SendMove);
						this.logic.changePlayer();
						this.state();
						System.out.println(this.logic.getGameState().toString());
					} else {
						System.out.println("False");
					}
				}
			} catch (PositionNotValidException | NoFigureOnPosition | NoKingOnTheBoardException | NoIdInListException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("Room - Incoming request code: " + req.getCodeRequest());
			break;
		}
	}

	private void state(){
		Logic.STATE state = null;
		try {
			state = this.logic.getGameState();
		} catch (PositionNotValidException | NoFigureOnPosition | NoKingOnTheBoardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Color currentPlayer = this.logic.getActualPlayer();
		switch (state  ){
		case CHECK:
			///
			break;
		case CHECK_MAT:
			this.win = currentPlayer == Color.White ? Color.Black : Color.White;
			this.addTask(Room.SendResult);
			break;
		case IN_GAME:
			break;
		case STALEMATE:
			this.win = null;
			this.addTask(Room.SendResult);
			break;
		default:
			break;

		}
	}

	public String getRoomName(){
		return this.name;
	}

	@Override
	protected void handleTheTask(Integer task) {
		if (task == Room.RefreshTables){
			Request req = new Request( RequestCodes.GAME_ROOM_LIST.getValue(), this.gameRoomTable);
			this.sendToAll(req);

			if (gameRoomTable.size() == Room.SizeOfRoom){
				boolean bStart = true;
				for (GameRoomTable table : gameRoomTable) {
					if (table.getReady() == false){
						bStart = false;
						break;
					}
				}
				if (bStart){
					logic = new Logic(new Board());
					Request request = new Request( RequestCodes.GAME_START.getValue(), logic.getBoard());
					this.sendToAll(request);
				}
			}
		} else if (task == Room.SendMove && !moveTosend.isEmpty()){
			Request req = new Request(504, moveTosend.poll());
			this.sendToAll(req);
		} else if (task == Room.SendResult){
			Request req = new Request(407, this.win);
			this.sendToAll(req);
			for (GameRoomTable gameRoomTable2 : gameRoomTable) {
				gameRoomTable2.setReady(false);
			}
			this.addTask(Room.RefreshTables);
		}
	}

	private void sendToAll(Request request){
		for (Client client : this.getClientList()) {
			client.addRequest(request);
		}
	}

	private void backToLobby(Request req, Client client){
		if (this.getParent().addClient(client) ){
			client.setReceiver(this.getParent());
			this.removeClient(client);
			client.addRequest(new Request(401, null));
			((Lobby) this.getParent()).addTask(Lobby.RefreshAllRoomList);
		} else {
			System.out.println("Room.backToLobby() Error");
		}
	}

	private GameRoomTable getClientGameRoomTable(int id){
		for (GameRoomTable item : gameRoomTable) {
			if (item.getId() == id){
				return item;
			}
		}
		return null;
	}

	private Color getColor(int id) throws NoIdInListException{
		for (GameRoomTable table : gameRoomTable) {
			if (id == table.getId()){
				return table.getLeftSide() ? Color.White : Color.Black;
			}
		}
		throw new NoIdInListException();
	}
}
