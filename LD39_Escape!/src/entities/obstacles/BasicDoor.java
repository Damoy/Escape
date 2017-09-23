package entities.obstacles;



import java.util.Iterator;

import graphics.Texture;
import world.Map;

public class BasicDoor extends Door{
	
	public BasicDoor(int row, int col, Map map) {
		super(Texture.BASIC_DOOR, row, col, map);
	}

	@Override
	public void update() {
	}
	
	public void open(){
		if(!opened){
			opened = true;
			
			Iterator<Obstacle> iterator = map.getObstacles().iterator();
			
			while(iterator.hasNext()){
				Obstacle next = iterator.next();
				
				if(next.equals(this)){
					iterator.remove();
					return;
				}
			}
		}
	}
	


}
