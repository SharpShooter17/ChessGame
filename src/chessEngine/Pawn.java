package chessEngine;

public class Pawn extends Figure {
	/**
	 *
	 */
	private static final long serialVersionUID = -9127553063030751034L;

	public Pawn(Color color) {
		super(color);
		this.firstMove = false;
	}

	private boolean firstMove;

	public boolean isFirstMove() {
		return firstMove;
	}
	public void setFirstMove(){
		this.firstMove = true;
	}
}
