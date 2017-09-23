package entities.items;

import graphics.Texture;
import world.Map;

public class BasicDoorKey extends Key{

	public BasicDoorKey(int row, int col, Map map) {
		super(Texture.BASIC_DOOR_KEY, row, col, map);
	}

	@Override
	public void update() {
		
	}

}
