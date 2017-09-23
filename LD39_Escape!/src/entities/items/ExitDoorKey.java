package entities.items;

import graphics.Texture;
import world.Map;

public class ExitDoorKey extends Key{

	public ExitDoorKey(int row, int col, Map map) {
		super(Texture.EXIT_DOOR_KEY, row, col, map);
	}

	@Override
	public void update() {
		
	}


}
