package entities.items;


import java.util.Iterator;
import java.util.List;

import entities.Player;
import graphics.Texture;
import toolbox.Logs;
import world.Map;

public class PowerBonus extends MapItem{

	private byte powerBonus;
	
	public PowerBonus(int row, int col, byte powerBonus, Map map) {
		super(Texture.POWER_COLLECT, row, col, map);
		this.powerBonus = powerBonus;
	}

	@Override
	public void update() {
		
	}
	
	public byte getPowerBonus(){
		return powerBonus;
	}
	
	public void collectPowerBonus(Player player, List<MapItem> mapItems){
		// map.powerTook = true;
		player.growPower(powerBonus);
		Logs.println("Power bonus acquired :  + " + powerBonus);
		
		Iterator<MapItem> iterator = mapItems.iterator();
		
		while(iterator.hasNext()){
			MapItem nextItem = iterator.next();
			
			if(nextItem.equals(this)){
				iterator.remove();
				return;
			}
		}
	}

}
