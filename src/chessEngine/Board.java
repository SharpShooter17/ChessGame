package chessEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements Serializable{
	private static final long serialVersionUID = 7961694445134951447L;
	public final static int BOARDX = 8;
	public final static int BOARDY = 8;
	private Figure[][] board;

	public Board() {
		board = new Figure[BOARDX][BOARDY];
		//Pawns
		for (int i = 0; i < BOARDX; i++){
			board[i][1] = new Pawn(Color.White);
			board[i][6] = new Pawn(Color.Black);
		}
		//Rooks
		board[0][0] = new Rook(Color.White);
		board[7][0] = new Rook(Color.White);
		board[0][7] = new Rook(Color.Black);
		board[7][7] = new Rook(Color.Black);
		//Horses
		board[1][0] = new Horse(Color.White);
		board[6][0] = new Horse(Color.White);
		board[1][7] = new Horse(Color.Black);
		board[6][7] = new Horse(Color.Black);
		//Bishops
		board[2][0] = new Bishop(Color.White);
		board[5][0] = new Bishop(Color.White);
		board[2][7] = new Bishop(Color.Black);
		board[5][7] = new Bishop(Color.Black);
		//King
		board[3][0] = new King(Color.White);
		board[3][7] = new King(Color.Black);
		//Queen
		board[4][0] = new Queen(Color.White);
		board[4][7] = new Queen(Color.Black);
	}

	public boolean isCheck(Color color) throws NoKingOnTheBoardException, PositionNotValidException, NoFigureOnPosition{
		Position king = this.findKingPosition(color);
		for (Position item : this.getListOfAllPossiblePlayerMoves((color == Color.Black ? Color.White : Color.Black))) {
			if ( item.equals(king) ){
				return true;
			}
		}

		return false;
	}

	public boolean isCheckMate(Color color) throws NoFigureOnPosition, PositionNotValidException, NoKingOnTheBoardException{
		List<Position> playerFigures = this.getListOfPlayerFigures(color);
		for (Position from : playerFigures) {
			List<Position> moves = this.makeListOfAvaivableMovements(from);
			for (Position to : moves) {
					Figure fig = this.getFigureFrom(to);
					this.move(from, to, true, null);
					if (!this.isCheck(color)){
						this.move(to, from, true, fig);
						return false;
					}

					this.move(to, from, true, fig);
			}
		}

		return true;
	}

	public boolean isStalemate(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		return this.getListOfAllPossiblePlayerMoves(color).size() == 0 ? true : false;
	}

	public List<Position> makeListOfAvaivableMovements(Position position) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		return makeListOfAvaivableMovements(position, true);
	}

	List<Position> makeListOfAvaivableMovements(Position position, boolean checkIfIsCheck) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		Figure figure = getFigureFrom(position);
		if (figure == null){
			return null;
		}
		List<Position> result = null;

		if (figure instanceof Pawn){
			result = availableMoveForPawn(position, figure.getColor());
		} else if (figure instanceof King){
			result = availableMoveForKing(position, figure.getColor());
		} else if (figure instanceof Horse){
			result = availableMoveForHorse(position, figure.getColor());
		} else if (figure instanceof Bishop) {
			result = availableMoveForBishop(position, figure.getColor());
		} else if (figure instanceof Rook){
			result = availableMoveForRook(position, figure.getColor());
		} else if (figure instanceof Queen){
			result = availableMoveForQueen(position, figure.getColor());
		}

		if (checkIfIsCheck){
			Color color = this.getFigureFrom(position).getColor();
			for (Iterator<Position> it = result.iterator(); it.hasNext(); ) {
				Position pos = it.next();
				Figure fig = this.getFigureFrom(pos);
				this.move(position, pos, true, null);
				boolean isCheck = isCheck(color);
				this.move(pos, position, true, fig);
				if (isCheck){
					it.remove();
				}
			}
		}

		return result;
	}

	public static boolean positionIsValid(Position position){
		if (position == null){
			return false;
		}

		if (position.getX() < BOARDX && position.getY() < BOARDY && position.getX() >= 0 && position.getY() >= 0){
			return true;
		}
		return false;
	}

	public Figure getFigureFrom(Position position) {
		if (positionIsValid(position)){
			return board[position.getX()][position.getY()];
		}
		return null;
	}

	public void move(Position from, Position to) throws PositionNotValidException, NoFigureOnPosition{
		this.move(from, to, false, null);
	}

	void move(Position from, Position to, boolean bTest, Figure toFig) throws PositionNotValidException, NoFigureOnPosition{
		if (!(positionIsValid(from) && positionIsValid(to))){
			throw new PositionNotValidException();
		}

		this.board[to.getX()][to.getY()] = this.board[from.getX()][from.getY()];
		this.board[from.getX()][from.getY()] = null;

		if (!bTest){
			Figure f = this.getFigureFrom(to);
			if (f == null ){
				throw new NoFigureOnPosition();
			} else if ( f instanceof Pawn ){
				((Pawn)f).setFirstMove();
			} else if (f instanceof King){
				((King)f).setFirstMove();
			} else if (f instanceof Rook){
				((Rook)f).setFirstMove();
			}
		} else {
			this.board[from.getX()][from.getY()] = toFig;
		}

	}

	public final Figure[][] getBoard(){
		return board;
	}

	private boolean isEnemy(Position p, Color myColor){
		Figure ftmp = getFigureFrom(p);
		if (ftmp != null){
			if (ftmp.getColor() == (myColor == Color.Black ? Color.White : Color.Black)){
				return true;
			}
		}
		return false;
	}

	private boolean isEmpty(Position p, Color myColor){
		Figure ftmp = getFigureFrom(p);
		if (ftmp == null){
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmptyPositionOrEnemy(Position p, Color color){
		Figure ftmp = getFigureFrom(p);
		if (ftmp != null){
			if (ftmp.getColor() == (color == Color.Black ? Color.White : Color.Black)){
				return true;
			}
		} else if (positionIsValid(p)){
			return true;
		}
		return false;
	}

	private List<Position> directMove(Position position, int directX, int directY){
		List<Position> result = new ArrayList<>();
		Color color = getFigureFrom(position).getColor();
		Position p = new Position(position.getX(), position.getY());
		while ( positionIsValid(p = new Position(p.getX() + directX, p.getY() + directY)) && getFigureFrom(p) == null )
	    {
	        result.add(p);
	    }
	    if ( positionIsValid(p) && getFigureFrom(p).getColor() != color ){
	        result.add(p);
	    }

		return result;
	}

	private List<Position> availableMoveForPawn(Position position, Color color){
		List<Position> result = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();
		int c = 0;

		if (color == Color.White){
			c = 1;
		} else {
			c = -1;
		}

		Position p = new Position(x, y + c);
		if ( positionIsValid(p) ){
			if (isEmpty(p, color)){
				result.add(p);
			}
		}
		p = new Position(x + 1, y + c);
		if ( positionIsValid(p) ){
			if (isEnemy(p, color)){
				result.add(p);
			}
		}
		p = new Position(x - 1, y + c);
		if ( positionIsValid(p) ){
			if (isEnemy(p, color)){
				result.add(p);
			}
		}
		Figure f = this.getFigureFrom(position);
		if ( f instanceof Pawn ){
			if ( !((Pawn)f).isFirstMove() ){
				p = new Position(x, y + 2*c);
				if ( positionIsValid(p) ){
					if (isEmpty(p, color)){
						result.add(p);
					}
				}
			}
		}

		return result;
	}

	private List<Position> availableMoveForKing(Position position, Color color){
		List<Position> result = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();

		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				Position p = new Position(x + i, y + j);
				if (isEmptyPositionOrEnemy(p, color) && !position.equals(p)){
					result.add(p);
				}
			}
		}

		return result;
	}

	private List<Position> availableMoveForHorse(Position position, Color color){
		List<Position> result = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();

		Position p[] = {new Position(x - 2, y + 1 ),
						new Position(x - 2, y - 1 ),
						new Position(x + 2, y + 1 ),
						new Position(x + 2, y - 1 ),
						new Position(x - 1, y + 2 ),
						new Position(x - 1, y - 2 ),
						new Position(x + 1, y + 2 ),
						new Position(x + 1, y - 2 ) };

		for (Position item : p) {
			if (isEmptyPositionOrEnemy(item, color)){
				result.add(item);
			}
		}

		return result;
	}

	private List<Position> availableMoveForBishop(Position position, Color color){
		List<Position> result = new ArrayList<>();

		result.addAll(directMove(position, -1, 1));
		result.addAll(directMove(position, -1, -1));
		result.addAll(directMove(position, 1, -1));
		result.addAll(directMove(position, 1, 1));

		return result;
	}

	private List<Position> availableMoveForRook(Position position, Color color){
		List<Position> result = new ArrayList<>();

		result.addAll(directMove(position, -1, 0));
		result.addAll(directMove(position, 1, 0));
		result.addAll(directMove(position, 0, 1));
		result.addAll(directMove(position, 0, -1));

		return result;
	}

	private List<Position> availableMoveForQueen(Position position, Color color){
		List<Position> result = new ArrayList<>();

		result.addAll(directMove(position, -1, 0));
		result.addAll(directMove(position, 1, 0));
		result.addAll(directMove(position, 0, 1));
		result.addAll(directMove(position, 0, -1));

		result.addAll(directMove(position, -1, 1));
		result.addAll(directMove(position, -1, -1));
		result.addAll(directMove(position, 1, -1));
		result.addAll(directMove(position, 1, 1));

		return result;
	}

	public Position findKingPosition(Color color) throws NoKingOnTheBoardException{
		for (int x = 0; x < BOARDX; x++){
			for (int y = 0; y < BOARDY; y++ ){
				Figure tmp = getFigureFrom(new Position(x,y));
				if (tmp instanceof King && tmp.getColor() == color){
					return new Position(x,y);
				}
			}
		}
		throw new NoKingOnTheBoardException();
	}

	public List<Position> getListOfAllPossiblePlayerMoves(Color color) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		List<Position> result = new ArrayList<>();
		List<Position> figuresList = getListOfPlayerFigures(color);

		for (Position position : figuresList) {
			result.addAll(makeListOfAvaivableMovements(position, false));
		}

		return result;
	}

	public List<Position> getListOfPlayerFigures(Color color){
		List<Position> result = new ArrayList<>();

		for (int x = 0; x < BOARDX; x++){
			for (int y = 0; y < BOARDY; y++ ){
				Figure figure = getFigureFrom(new Position(x,y));
				if (figure == null){
					continue;
				}
				if ( figure.getColor() == color ){
					result.add(new Position(x,y));
				}
			}
		}

		return result;
	}
}
