package chessEngine;

import java.io.Serializable;

public abstract class Figure  implements Serializable {
	private static final long serialVersionUID = 7456131867895883971L;
	private Color color;
	public Figure(Color color){
		setColor(color);
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
