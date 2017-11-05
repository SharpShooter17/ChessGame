package chessEngine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum Color implements Serializable {
	Black(0),
	White(1);

	private final int value;

	private static Map<Integer, Color> map = new HashMap<Integer, Color>();

    static {
        for (Color colorEnum : Color.values()) {
            map.put(colorEnum.value, colorEnum);
        }
    }

    public static Color valueOf(int color) {
        return map.get(color);
    }

	Color(int val){
		this.value = val;
	}

	public int getValue(){
		return this.value;
	}
}
