package common;

import java.io.Serializable;

import chessEngine.Position;

public class Move implements Serializable {

	private static final long serialVersionUID = -1020987997694481293L;

	public Position from;
	public Position to;

	public Move(Position from, Position to){
		this.from = from;
		this.to = to;
	}
}
