package entities.items;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import world.Map;

public abstract class Key extends MapItem{

	public Key(BufferedImage texture, int row, int col, Map map) {
		super(texture, row, col, map);
	}

	public void collectKey(List<MapItem> items){
		Iterator<MapItem> iterator = items.iterator();
		
		while(iterator.hasNext()){
			MapItem nextItem = iterator.next();
			
			if(nextItem.equals(this)){
				iterator.remove();
				return;
			}
		}
	}

}
