package entities.obstacles;

import java.awt.image.BufferedImage;

import world.Map;

public abstract class Door extends Obstacle{

	protected boolean opened;
	protected Map map;
	
	public Door(BufferedImage texture, int row, int col, Map map) {
		super(texture, Obstacle.NONE, row, col);
		this.map = map;
	}
	

	public abstract void open();
	public boolean canWalkThrough(){
		return opened;
	}
	
	@Override
	protected void setTexture() {
		
	}

	public boolean isOpened() {
		return opened;
	}


	public Map getMap() {
		return map;
	}
	
	
	
}
