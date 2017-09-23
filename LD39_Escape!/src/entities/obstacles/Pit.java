package entities.obstacles;


import graphics.Texture;

public class Pit extends Obstacle{

	public Pit(int row, int col) {
		super(Texture.PIT, Obstacle.NONE, row, col);
	}

	@Override
	protected void setTexture() {
		
	}

	@Override
	public void update() {
		
	}

}
