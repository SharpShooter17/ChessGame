package chessEngine;

import java.util.List;

public class Logic {
	private Color actualPlayer;
	private Board board;

	public enum STATE {IN_GAME, CHECK, CHECK_MAT, STALEMATE };

	public Logic(Board board) {
		actualPlayer = Color.White;
		this.board = board;
	}

	public STATE getGameState() throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		STATE result = STATE.IN_GAME;

		if ( this.getBoard().isCheckMate(this.actualPlayer) ){
			result = STATE.CHECK_MAT;
		} else if ( this.getBoard().isCheck(this.actualPlayer) ){
			result = STATE.CHECK;
		} else if (this.getBoard().isStalemate(this.actualPlayer)){
			result = STATE.STALEMATE;
		}

		return result;
	}

	public boolean moveIsValid(Position from, Position to) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException {
		if ( Board.positionIsValid(from) == false || Board.positionIsValid(to) == false){
			return false;
		}

		Figure fig = this.board.getFigureFrom(from);
		if ( fig != null ){
			if (fig.getColor() != this.actualPlayer){
				return false;
			}
		} else {
			return false;
		}

		List<Position> pos = board.makeListOfAvaivableMovements(from);

		if (pos == null){
			return false;
		}

		for (Position position : pos) {
			System.out.println("[" + position.getX() + ", " + position.getY() + "]");
		}

		for (Position position : pos) {
			if (to.equals(position)){
				return true;
			}
		}
		return false;
	}

	public boolean isPossibleShortCastling(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		int [] x = { 1, 2 };
		try {
			return this.isPossibleCastling(color, true, x);
		} catch (NotValidParameterException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isPossibleLongCastling(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		int [] x = { 4, 5, 6 };
		try {
			return this.isPossibleCastling(color, true, x);
		} catch (NotValidParameterException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isPossibleCastling(Color color, boolean bShort, int [] nullPositions) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException, NotValidParameterException{
		if (nullPositions.length < 2){
			throw new NotValidParameterException();
		}

		Figure king = null;
		Figure rook = null;

		int y = color == Color.White ? 0 : 7;

		king = this.getBoard().getBoard()[3][y];
		rook = this.getBoard().getBoard()[0][y];

		for (int i : nullPositions) {
			if ( this.getBoard().getBoard()[i][y] != null ){
				return false;
			}
		}

		if ( king instanceof King && rook instanceof Rook ){
			King k = (King) king;
			Rook r = (Rook) rook;

			if (k.isFirstCheck() == false && k.isFirstMove() == false && r.isFirstMove() == false){
				List<Position> list = this.getBoard().getListOfAllPossiblePlayerMoves(color == Color.White ? Color.Black : Color.White);

				Position pos1 = new Position(nullPositions[0], y);
				Position pos2 = new Position(nullPositions[1], y);

				for (Position position : list) {
					if ( position.equals(pos1) || position.equals(pos2) ){
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	public boolean makeShortCastling(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		if (!isPossibleShortCastling(color) ) {
			return false;
		}

		int y = color == Color.White ? 0 : 7;

		this.getBoard().move(new Position(3,y ), new Position(1, y));
		this.getBoard().move(new Position(0,y ), new Position(2, y));

		return true;
	}

	public boolean makeLongCastling(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		if (!isPossibleLongCastling(color) ) {
			return false;
		}

		int y = color == Color.White ? 0 : 7;

		this.getBoard().move(new Position(3, y), new Position(5, y));
		this.getBoard().move(new Position(7, y), new Position(4, y));

		return true;
	}

	public Color getActualPlayer() {
		return this.actualPlayer;
	}

	public void setActualPlayer(Color actualPlayer) {
		this.actualPlayer = actualPlayer;
	}

	public boolean makeMove(Position from, Position to) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException {
		if ( this.moveIsValid(from, to) ){
			this.board.move(from, to);
			return true;
		}
		return false;
	}

	public void changePlayer(){
		this.actualPlayer = (this.actualPlayer == Color.White ? Color.Black : Color.White);
	}

	public Board getBoard(){
		return this.board;
	}
}
