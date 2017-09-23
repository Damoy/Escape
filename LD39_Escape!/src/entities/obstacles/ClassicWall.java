package entities.obstacles;


import graphics.Texture;

public class ClassicWall extends Obstacle{

	public ClassicWall( byte type, int row, int col) {
		super(Texture.GRAY_GROUND, type, row, col); // texture auto set in constructor
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void setTexture(){
		switch(type){
			case Obstacle.TOP_LEFT:
				texture = Texture.WALL1_TOP_LEFT;
				break;
			case Obstacle.TOP_RIGHT:
				texture = Texture.WALL1_TOP_RIGHT;
				break;
			case Obstacle.BOT_LEFT:
				texture = Texture.WALL1_BOT_LEFT;
				break;
			case Obstacle.BOT_RIGHT:
				texture = Texture.WALL1_BOT_RIGHT;
				break;
			case Obstacle.CENTER:
				texture = Texture.WALL1_CENTER;
				break;
		}
	}
}
