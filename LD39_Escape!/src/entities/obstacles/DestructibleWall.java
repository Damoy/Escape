package entities.obstacles;

import graphics.Texture;

public class DestructibleWall extends Obstacle{

	public DestructibleWall(int row, int col) {
		super(Texture.DESTRUCTIBLE_WALL_CENTER, Obstacle.NONE, row, col); // texture auto set in constructor
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void setTexture(){

	}
}
