package entities.obstacles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import graphics.Renderer;
import physics.Collision;
import world.Map;

public abstract class Obstacle extends Entity{
	
	public final static byte TOP_LEFT = (byte) 1;
	public final static byte TOP_RIGHT = (byte) 2;
	public final static byte BOT_LEFT = (byte) 3;
	public final static byte BOT_RIGHT = (byte) 4;
	public final static byte CENTER = (byte) 5;
	public final static byte NONE = (byte) 42;
	
	protected byte type;
	
	public Obstacle(BufferedImage texture, byte type, int row, int col) {
		super(texture, col * Map.TILE_SIZE, row * Map.TILE_SIZE);
		this.type = type;
		setTexture();
		updateBox();
	}
	
	public void render(Graphics2D g){
		Renderer.basicRender(g, texture, x, y);
	}
	
	protected abstract void setTexture();
	
	
	public static Obstacle getObstacleCollision(Player player, float x, float y, List<Obstacle> obstacles){
		Iterator<Obstacle> iterator = obstacles.iterator();
		
		while(iterator.hasNext()){
			Obstacle obstacle = iterator.next();
			
			if(Collision.boxCollide(obstacle.getBox(), x, y, player.getWidth(), player.getHeight())){
				return obstacle;
			}
		}
		return null;
	}
	
}
