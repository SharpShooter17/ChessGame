package common;

import java.io.Serializable;

public class Space implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Space(String name, int id, int players){
		this.id = id;
		this.name = name;
		this.players = players;
	}
	public String name;
	public int id;
	public int players;
}
