package chessEngine;

public class King extends Figure {
	private static final long serialVersionUID = -2653618562952990777L;

	public King(Color color) {
		super(color);
		this.firstMove = false;
		this.firstCheck = false;
	}

	private boolean firstMove;
	private boolean firstCheck;

	public boolean isFirstMove() {
		return firstMove;
	}
	public void setFirstMove(){
		this.firstMove = true;
	}
	public boolean isFirstCheck() {
		return firstCheck;
	}
	public void setFirstCheck() {
		this.firstCheck = true;
	}
}
