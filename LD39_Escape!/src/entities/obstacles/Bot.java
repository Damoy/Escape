package entities.obstacles;


import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import gameTester.Game;
import graphics.Renderer;
import graphics.Texture;
import physics.Collision;
import sound.Sound;
import toolbox.Logs;
import toolbox.time.Timer;
import world.Map;

public class Bot extends Obstacle{

	private List<BotProjectile> projectiles;
	private List<BotProjectile> projectilesToRemove;
	
	private Player player;
	private Map map;
	private Timer generationProjectileTimer;
	private byte direction;
	private boolean canGenProj;
	
	// botside should be botProjectile data
	public Bot(Player player, Map map, int row, int col, byte botSide) {
		super(Texture.BOT_TOP, Obstacle.NONE, row, col);
		projectiles = new ArrayList<>();
		projectilesToRemove = new ArrayList<>();
		
		generationProjectileTimer = new Timer(1);
		canGenProj = true;
		this.map = map;
		this.player = player;
		this.direction = botSide;
		Logs.println("generated");
		setTexture2();
	}
	
	private void setTexture2(){
		switch(direction){
			case BotProjectile.GOING_DOWN:
				setTexture(Texture.BOT_TOP);
				break;
			case BotProjectile.GOING_LEFT:
				setTexture(Texture.BOT_LEFT);
				break;
			case BotProjectile.GOING_RIGHT:
				setTexture(Texture.BOT_RIGHT);
				break;
			case BotProjectile.GOING_UP:
				setTexture(Texture.BOT_DOWN);
				break;
		}
	}

	@Override
	protected void setTexture() {
	}

	@Override
	public void update() {
		genProjectiles();
		updateProjectiles();
		//printProjectiles();
		removeProjectiles();
		updateBox();
	}
	
	public void render(Graphics2D g){
		super.render(g);
		renderProjectiles(g);
	}
	
	private void renderProjectiles(Graphics2D g){
		for(BotProjectile projectile : projectiles){
			projectile.render(g);
		}
	}
	
	private void printProjectiles(){
		Logs.println("bot projectiles size " + projectiles.size());
	}
	
	private float getSideX(){
		switch(direction){
			case BotProjectile.GOING_DOWN:
				return x + 4;
			case BotProjectile.GOING_LEFT:
				return x - 1;
			case BotProjectile.GOING_UP:
				return x + 4;
			case BotProjectile.GOING_RIGHT:
				return x + 9;
		}
		return x + 4;
	}
	
	private float getSideY(){
		switch(direction){
			case BotProjectile.GOING_DOWN:
				return y + 9;
			case BotProjectile.GOING_LEFT:
				return y + 4;
			case BotProjectile.GOING_UP:
				return y - 1;
			case BotProjectile.GOING_RIGHT:
				return y + 4;
		}
		return y + 4;
	}
	
	
	private void genProjectiles(){
		if(generationProjectileTimer == null || !canGenProj){
			//Logs.println("returned");
			return;
		}
		
		generationProjectileTimer.increment();
		
		if(generationProjectileTimer.getTimeLeft() <= 0){
			generationProjectileTimer.reset();
			canGenProj = true;
			projectiles.add(new BotProjectile(this, player, map, getSideX(), getSideY(), direction));
		}
	}
	
	private void updateProjectiles(){
		for(BotProjectile projectile : projectiles){
			projectile.update();
		}
	}
	
	public void removeProjectiles(){
		Iterator<BotProjectile> iterator = projectilesToRemove.iterator();
		while(iterator.hasNext()){
			BotProjectile projectile = iterator.next();
			
			projectiles.remove(projectile);
			iterator.remove();
		}
	}
	
	public void addToRemoveProjectiles(BotProjectile projectile){
		projectilesToRemove.add(projectile);
	}
	
	public class BotProjectile extends Entity{
		public final static byte GOING_LEFT = (byte) 1;
		public final static byte GOING_RIGHT = (byte) 2;
		public final static byte GOING_UP = (byte) 3;
		public final static byte GOING_DOWN = (byte) 4;
		
		private byte side;
		
		private Map map;
		private Player player;
		private Bot bot;
		private List<Obstacle> mapObstacles;
		
		public BotProjectile(Bot bot, Player player, Map map, float x, float y, byte side) {
			super(Texture.PLAYER_PROJECTILE, x, y);
			this.bot = bot;
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
		
		private void checkCollision(){
			checkPlayerCollision();
			checkObstaclesCollision();
		}
		
		private void checkPlayerCollision(){
			if(Collision.boxCollide(box, player.getBox())){
				Sound.loseHit.play();
				// JukeBox.playLose();
				removeThis();
				map.reset();
			}
		}
		
		private void checkObstaclesCollision(){
			Iterator<Obstacle> iterator = mapObstacles.iterator();
			
			while(iterator.hasNext()){
				Obstacle next = iterator.next();
				
				if(Collision.boxCollide(box, next.getBox())){
					removeThis();
				}
			}
		}
		
		private void removeThis(){
			bot.addToRemoveProjectiles(this);
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

}


