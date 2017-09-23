package entities.playerData;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.obstacles.Bot;
import entities.obstacles.DestructibleWall;
import entities.obstacles.Obstacle;
import gameTester.Game;
import graphics.Renderer;
import graphics.Texture;
import physics.Collision;
import sound.Sound;
import world.Map;

public class Projectile extends Entity{
	
	public final static byte GOING_LEFT = (byte) 1;
	public final static byte GOING_RIGHT = (byte) 2;
	public final static byte GOING_UP = (byte) 3;
	public final static byte GOING_DOWN = (byte) 4;
	
	private byte side;
	
	private Map map;
	private Player player;
	private List<Obstacle> mapObstacles;
	
	public Projectile(Player player, Map map, float x, float y, byte side) {
		super(Texture.PLAYER_PROJECTILE, x, y); //  row * Map.TILE_SIZE, col * Map.TILE_SIZE
		this.player = player;
		this.map = map;
		this.mapObstacles = map.getObstacles();
		this.side = side;
	}

	@Override
	public void update() {
		checkCollision();
		updatePos();
		updateBox();
		checkRemove();
	}
	
	private void checkRemove(){
		if(x < 0 || x + 1 > Game.WIDTH || y < 0 || y + 1 > Game.HEIGHT){
			removeThis();
		}
	}
	
	private boolean obstacleRemoved = false;
	
	private void checkCollision(){
		Iterator<Obstacle> iterator = mapObstacles.iterator();
		
		Obstacle toRemove = null;
		
		while(iterator.hasNext()){
			
			Obstacle next = iterator.next();
			
//			if((!(next instanceof DestructibleWall) && (!(next instanceof Bot)))){
//				continue;
//			}
			
			if(Collision.boxCollide(box, next.getBox())){
				if((!(next instanceof DestructibleWall) && (!(next instanceof Bot)))){
					obstacleRemoved = true;
					break;
				}
				// JukeBox.playLose(); // is enemy hit
				Sound.loseHit.play();
				toRemove = next;
				break;
			}
		}
		
		iterator = mapObstacles.iterator();
		// remove the obstacle
		while(iterator.hasNext()){
			
			Obstacle next = iterator.next();
			
			if(next.equals(toRemove)){
				iterator.remove();
				obstacleRemoved = true;
			}
		}
		
		if(obstacleRemoved){
			removeThis();
		}
	}
	
	private void removeThis(){
		player.addToRemoveProjectiles(this);
//		Iterator<Projectile> iterator = player.getProjectiles().iterator();
//		
//		while(iterator.hasNext()){
//			
//			Projectile next = iterator.next();
//			
//			if(next.equals(this)){
//				iterator.remove();
//			}
//		}
	}
	
	private void updatePos(){
		switch(side){
			case GOING_LEFT:
				x--;
				break;
			case GOING_DOWN:
				y++;
				break;
			case GOING_RIGHT:
				x++;
				break;
			case GOING_UP:
				y--;
				break;
		}
	}

	@Override
	public void render(Graphics2D g) {
		Renderer.basicRender(g, texture, x, y);
	}

	public byte getSide() {
		return side;
	}

	public void setSide(byte side) {
		this.side = side;
	}

	
	
}
