package client;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class GameRoomTableView {
	private SimpleStringProperty name;
	private SimpleBooleanProperty ready;

	public GameRoomTableView(String name, boolean ready){
		this.name = new SimpleStringProperty(name);
		this.ready = new SimpleBooleanProperty(ready);
	}

	public String getName() {
		return name.get();
	}
	public Boolean getReady() {
		return this.ready.get();
	}
	public void setReady(boolean bReady) {
		this.ready.set(bReady);
	}
}
