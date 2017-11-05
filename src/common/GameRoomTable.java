package common;

import java.io.Serializable;

public class GameRoomTable implements Serializable {
	private static final long serialVersionUID = -8012025170211151373L;

	private final String name;
	private boolean ready;
	private final boolean leftSide;
	private final int id;
 	public GameRoomTable(String name, boolean ready, boolean leftSide, int id) {
 		this.name = name;
 		this.ready = ready;
 		this.leftSide = leftSide;
 		this.id = id;
 	}
	public String getName() {
		return name;
	}
	public boolean getReady() {
		return ready;
	}
	public void setReady(boolean b){
		this.ready = b;
	}
	public boolean getLeftSide() {
		return leftSide;
	}
	public int getId() {
		return id;
	}
}
