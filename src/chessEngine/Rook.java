package chessEngine;

public class Rook extends Figure{

	private static final long serialVersionUID = -1495379201324408067L;

	public Rook(Color color) {
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