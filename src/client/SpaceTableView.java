package client;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpaceTableView {

	private SimpleStringProperty name;
	private SimpleIntegerProperty players;
	private SimpleIntegerProperty id;

	public SpaceTableView(String name, int players, int id){
		this.name = new SimpleStringProperty(name);
		this.players = new SimpleIntegerProperty(players);
		this.id = new SimpleIntegerProperty( id );
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Integer getPlayers() {
		return players.get();
	}

	public void setPlayers(int players) {
		this.players.set(players);
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set( id );
	}
}
