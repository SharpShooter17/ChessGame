package chessEngine;

import java.io.Serializable;

public class Position implements Serializable{
	private static final long serialVersionUID = 9121808923818220785L;
	private int x;
	private int y;
	public Position(){
		setX(0);
		setY(0);
	}
	public Position(int x, int y){
		setX(x);
		setY(y);
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public boolean equals(Position p){
		return (this.getX() == p.getX() && this.getY() == p.getY());
	}
}
