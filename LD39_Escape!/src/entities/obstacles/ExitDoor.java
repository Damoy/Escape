package entities.obstacles;


import java.awt.image.BufferedImage;

import graphics.Texture;
import world.Map;

public class ExitDoor extends Door{

	private BufferedImage openedTexture = Texture.EXIT_FREE;
	
	public ExitDoor(int row, int col, Map map) {
		super(Texture.EXIT_DOOR, row, col, map);
		if(map.getLevel() == Map.LEVEL_9){
			texture = Texture.EXIT_DOOR_END;
		}
	}

	@Override
	public void update() {
		if(opened){
			if(texture != openedTexture){
				texture = Texture.EXIT_FREE;
			}
		}
	}
	
	public void open(){
		if(!opened){
			texture = openedTexture;
			opened = true;
		}
	}
	
}
