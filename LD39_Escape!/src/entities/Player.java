package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import entities.items.Key;
import entities.items.PowerBonus;
import entities.obstacles.Door;
import entities.obstacles.ExitDoor;
import entities.obstacles.Obstacle;
import entities.obstacles.Pit;
import entities.playerData.Power;
import entities.playerData.Projectile;
import gameTester.Game;
import graphics.Renderer;
import graphics.Texture;
import input.Keys;
import physics.Collision;
import sound.Sound;
import toolbox.Logs;
import toolbox.time.Timer;
import world.Map;

public class Player extends Entity{
	
	private Game game;
	private Power power;
	private Map map; // need setMap
	
	private int rowSave;
	private int colSave;
	public boolean mapUnset;
	
	private List<Projectile> projectiles;
	
	public Player(Game game, int row, int col) {
		super(Texture.PLAYER_LOOK_BOT, col * Map.TILE_SIZE, row * Map.TILE_SIZE);
		this.game = game;
		this.row = row;
		this.col = col;
		mapUnset = true;
		powered = true;
		projectiles = new ArrayList<>();
		power = new Power(this, new Font("Tahoma", Font.PLAIN, 8), 5,
				Game.WIDTH * 8/100, Game.HEIGHT * 88/100);
	}
	
	
	@Override
	public void update(){
		if(!canRenderPower){
			updateEndChangeTextureTimer();
		}
		if(!mapCanMove)return;
		
		printTiledMapPos();
		checkDeathTimerState();
		updateTimerShoot();
		
		checkProjectileGeneration();
		
		handleMovement();
		handlePitCollision();
		// checkTextureCollision();
		checkItemsCollision();
		updatePower();
		
		updateBox();
		
		updateProjectiles();
		removeProjectiles();
	}
	
//	private void checkTextureCollision(){
//		if((movingTo == UP || movingTo == DOWN) && nextPosCollision){
//			changeTexture(movingTo);
//		}
//	}
	
	public void removeProjectiles(){
		Iterator<Projectile> iterator = toRemoveProjectiles.iterator();
		while(iterator.hasNext()){
			Projectile projectile = iterator.next();
			
			projectiles.remove(projectile);
			iterator.remove();
		}
	}
	
	private List<Projectile> toRemoveProjectiles = new ArrayList<>();
	
	public List<Projectile> getToRemoveProjectiles(){
		return toRemoveProjectiles;
	}
	
	public void addToRemoveProjectiles(Projectile projectile){
		if(toRemoveProjectiles.contains(projectile)) return;
		toRemoveProjectiles.add(projectile);
	}
	
	private Timer timerShoot;
	private boolean timerShootGenerated;
	private boolean canShoot = true;
	
	private void checkProjectileGeneration(){
		if(!canShoot) return;
//		if(Keys.isPressed(Keys.SPACE) && leftKeysPressed()){ // TODO REMOVED FOR NOW
//			projectiles.add(new Projectile(x - 1, y + 4, Projectile.GOING_LEFT));
//		}
//		else if(Keys.isPressed(Keys.SPACE) && rightKeysPressed()){
//			projectiles.add(new Projectile(x + 9, y + 4, Projectile.GOING_RIGHT)); // 9 == 8 + 1
//		}
		// else 
		if(Keys.isPressed(Keys.SPACE)){
			
			Sound.playerShoot.play();
			//	JukeBox.playPlayerShoot();
			
			if(lookingTo == LOOKING_BOT){
				projectiles.add(new Projectile(this, map, x + 4, y + 9, Projectile.GOING_DOWN));
			}
			else if(lookingTo == LOOKING_TOP){
				projectiles.add(new Projectile(this, map, x + 4, y - 1, Projectile.GOING_UP));
			}
			else{
				projectiles.add(new Projectile(this, map, x + 4, y + 9, Projectile.GOING_DOWN));
			}
			genTimerShoot();
			power.usePower(2); // shoot uses POWER
		}
		
	}
	
	private void genTimerShoot(){
		if(!timerShootGenerated){
			timerShoot = new Timer(1);
			timerShootGenerated = true;
			canShoot = false;
		}
	}
	
	private void updateTimerShoot(){
		if(!timerShootGenerated || timerShoot == null) return;
		
		timerShoot.increment();
		if(timerShoot.getTimeLeft() <= 0){
			timerShoot = null;
			canShoot = true;
			timerShootGenerated = false;
		}
	}
	
	private void updateProjectiles(){
		for(Projectile projectile : projectiles){
			projectile.update();
		}
	}
	
	private void updatePower(){
		power.update(); // TODO EMPTY NOW
	}
	
	private void printTiledMapPos(){
		Logs.println("row : " + row + ", col : " + col);
	}
	
	
	private boolean canMove = true;
	private final static byte LEFT = (byte) 11;
	private final static byte RIGHT = (byte) 12;
	private final static byte UP = (byte) 13;
	private final static byte DOWN = (byte) 14;
	private final static byte NONE = (byte) 15;
	
	private final static byte LOOKING_TOP = (byte) 40;
	private final static byte LOOKING_BOT = (byte) 41;
	
	private byte lookingTo = NONE;
	private byte movingTo = NONE;
	private boolean movingToSet = false;
	
	private byte cptUpdateX = 0;
	private byte cptUpdateY = 0;
	
	
	private void checkItemsCollision(){
		checkPowerBonusCollision();
		checkKeyCollision();
		checkExitDoorCollision();
	}
	
	private void checkKeyCollision(){
		java.util.Map<Key, Door> doors_data = map.getDoorData();
		
		Iterator<Entry<Key, Door>> iterator = doors_data.entrySet().iterator();
		
		while(iterator.hasNext()){
			Entry<Key, Door> entry = iterator.next();
			
			Key key = entry.getKey();
			Door door = entry.getValue();
			
			if(Collision.boxCollide(box, key.getBox())){
				Sound.doorOpen.play();
				// JukeBox.playDoorSound();
				door.open();
				key.collectKey(map.getItems());
				
				doors_data.remove(key);
				//iterator.remove();
				return;
			}
		}
	}
	
	private boolean stop = false;
	private boolean canRenderPower = true;
	private Timer endChangeTextureTimer;
	private boolean canUpdateEndChangeTextureTimer;
	
	private void stop(){
		// stop = true;
		mapCanMove = false;
		stopRenderPower();
		endChangeTextureTimer = new Timer(1);
		canUpdateEndChangeTextureTimer = true;
	}
	
	private void updateEndChangeTextureTimer(){
		if(!canUpdateEndChangeTextureTimer || endChangeTextureTimer == null) return;
		
		endChangeTextureTimer.increment();
		
		if(endChangeTextureTimer.getTimeLeft() <= 0){
			canUpdateEndChangeTextureTimer = true;
			endChangeTextureTimer = new Timer(1);
			
			if(texture == Texture.PLAYER_LOOK_BOT){
				texture = Texture.PLAYER_LOOK_BOT2;
			}
			else{
				texture = Texture.PLAYER_LOOK_BOT;
			}
		}
	}
	
	private void stopRenderPower(){
		canRenderPower = false;
	}
	
	
	private void checkExitDoorCollision(){
		// TODO SHOULD ONLY HAVE ONE EXIT DOOR
		ExitDoor exitDoor = map.getExitDoor();
		
		if(exitDoor == null) return;
			
		if(exitDoor.canWalkThrough() && Collision.boxCollide(box, exitDoor.getBox())){
			byte next_level = (byte) (map.getLevel() + 1);
			
			Sound.goingThroughDoor.play();
			// JukeBox.playEnterDoorSound();
			
			if(next_level > Map.LEVEL_MAX){
				map.setLevel((byte) -1);
				map.endGame();
				stop();
			}
			else{
				//map = new Map(game, this, next_level, map.getMaxWidth(), map.getMaxHeight());
				map.setLevel(next_level);
			}
		}
	}
	
	private void checkPowerBonusCollision(){
		List<PowerBonus> powerBonuss = map.getPowerBonuss();
		
		Iterator<PowerBonus> iterator = powerBonuss.iterator();
		
		while(iterator.hasNext()){
			PowerBonus powerBonus = iterator.next();
			
			if(Collision.boxCollide(box, powerBonus.getBox())){
				Sound.powerup.play();
				// JukeBox.playPowerUp();
				powerBonus.collectPowerBonus(this, map.getItems());
			}
		}
	}
	
	public boolean mapCanMove = true;
	
	private boolean powered; // power left ?
	
	public void resetPower(){
		power.reset();
	}
	
	public void setPower(int powerValue){
		power.setPower(powerValue);
	}
	
	public void growPower(int power){
		this.power.growPower(power);
	}
	
	public void hasNoMorePower(){
		powered = false;
		startDeathTimer();
	}
	
	private Timer deathTimer;
	private boolean canUpdateDeathTimer;
	private boolean deathTimerStarted;
	private final static byte DEATH_TIMER_DELAY = 3;
	
	private void startDeathTimer(){
		if(!deathTimerStarted){
			deathTimer = new Timer(DEATH_TIMER_DELAY);
			canUpdateDeathTimer = true;
			deathTimerStarted = true;
		}
	}
	
	private void checkDeathTimerState(){
		if(!canUpdateDeathTimer){
			return;
		}
		deathTimer.increment();
		Logs.println("deathTimer incremented !");
		renderDeathScreen();
		if(deathTimer.getTimeLeft() <= 0){
			Logs.println("RESETTTTTT");
			// reset();
			// map = new Map(game, this, map.getLevel(), map.getMaxWidth(), map.getMaxHeight());
			// reset();
			map.reset();
			deathTimer = null;
			canUpdateDeathTimer = false;
			canRenderDeathScreen = false;
			deathTimerStarted = false;
		}
	}
	
	public void reset(){
		resetPower();
		resetPos();
		canMove = true;
		powered = true;
		lookingTo = LOOKING_BOT;
		texture = Texture.PLAYER_LOOK_BOT;
		mapUnset = true;
		Logs.println("LOOKING TO AFTER RESET" + lookingTo);
	}
	
	public void resetPos(){
		row = rowSave;
		col = colSave;
		x = col * Map.TILE_SIZE;
		y = row * Map.TILE_SIZE;
	}
	
	private boolean canRenderDeathScreen;
	
	private void renderDeathScreen(){
		if(canRenderDeathScreen) return;
		canRenderDeathScreen = true;
	}
	
	// private boolean nextPosCollision;
	
	private void handleMovement(){
		float dx = 0;
		float dy = 0;
		boolean usePower = false;
		
		if(canMove && powered){ // TODO NEW :: powered
			if(leftKeysPressed()){
				movingTo = LEFT;
				movingToSet = true;
				usePower = true;
				mapUnset = false;
			}
			
			if(rightKeysPressed()){
				movingTo = RIGHT;
				movingToSet = true;
				usePower = true;
				mapUnset = false;
			}
			
			if(upKeysPressed()){
				movingTo = UP;
				movingToSet = true;
				lookingTo = LOOKING_TOP;
				usePower = true;
				mapUnset = false;
			}
			
			if(downKeysPressed()){
				movingTo = DOWN;
				movingToSet = true;
				lookingTo = LOOKING_BOT;
				usePower = true;
				mapUnset = false;
			}
		}
		
		if(movingTo == LEFT){
			dx = -1;
			cptUpdateX++;
		}
		else if(movingTo == RIGHT){
			dx = 1;
			cptUpdateX++;
		}
		else if(movingTo == UP){
			dy = -1;
			cptUpdateY++;
		}
		else if(movingTo == DOWN){
			dy = 1;
			cptUpdateY++;
		}
		
		if(movingToSet && (movingTo == LEFT || movingTo == RIGHT)){
			float next_x = x + dx;
			
			if(next_x < 0 || next_x + getWidth() > Game.WIDTH){
				//Logs.println("SCREEN'S BORDER");
				resetColMoveData();
				return;
			}
			
			Obstacle obstacle = Obstacle.getObstacleCollision(this, next_x, y, map.getObstacles());
			//boolean timeToReset = false;
			
			if(obstacle != null){
				ExitDoor door = map.getExitDoor();
				boolean collision = true;
				// nextPosCollision = true;
				usePower = false;
				
				if(door == null){
					Logs.println("DOOR IS NULL");
				}
				
				if(obstacle.equals(door)){
					Logs.println("OBSTACLE EQUALS DOOR");
					
//					if(door instanceof ExitDoor){
//						resetTextureAndAssociatedData();
//					}
					
					if(door.canWalkThrough()){
						Logs.println("DOOR CAN WALK THROUGH");
						collision = false;
						//nextPosCollision = false;
					}
				}
				else if(obstacle instanceof Pit){
					collision = false;
					//timeToReset = true;
				}
				
				resetColMoveData();
				
				if(collision){
					return;
				}
			}
			
			handlePowerUse(usePower);
			handleTextureChange();
			
			if(cptUpdateX <  Map.TILE_SIZE && movingToSet){
				canMove = false;
			}
			else{
				if(next_x < x){
					setNewCol(-1);
				}
				
				else if(next_x > x){
					setNewCol(1);
				}
				
				cptUpdateX = 0;
				movingToSet = false;
			}
			
			x = next_x;
			
			if(cptUpdateX >= Map.TILE_SIZE){
				startDeathTimer();
			}
		}
		
		if(movingToSet && (movingTo == DOWN || movingTo == UP)){
			float next_y = y + dy;
			
			if(next_y < 0 || next_y + getHeight() > Game.HEIGHT){
				//Logs.println("SCREEN'S BORDER");
				resetRowMoveData();
				return;
			}
			
			Obstacle obstacle = Obstacle.getObstacleCollision(this, x, next_y, map.getObstacles());

			if(obstacle != null){
				ExitDoor door = map.getExitDoor();
				boolean collision = true;
				// nextPosCollision = true;
				usePower = false;
				
				if(door == null){
					Logs.println("DOOR IS NULL");
				}
				
				if(obstacle.equals(door)){
					Logs.println("OBSTACLE EQUALS DOOR");
					
					if(door.canWalkThrough()){
						Logs.println("DOOR CAN WALK THROUGH");
						collision = false;
						// nextPosCollision = false;
					}
				}
				else if(obstacle instanceof Pit){
					collision = false;
					//timeToReset = true;
				}
				
				resetRowMoveData();
				
				if(collision){
					return;
				}
			}
			
			handlePowerUse(usePower);
			handleTextureChange();
			
			if(cptUpdateY <  Map.TILE_SIZE && movingToSet){
				canMove = false;
			}
			else{
				if(next_y < y){
					setNewRow(-1);
				}
				
				else if(next_y > y){
					setNewRow(1);
				}
				
				cptUpdateY = 0;
				movingToSet = false;
			}
			
			y = next_y;
		}
	}
	
	
	private void handlePitCollision(){
		Iterator<Obstacle> iterator = map.getObstacles().iterator();
		
		while(iterator.hasNext()){
			Obstacle next = iterator.next();
			
			if(next instanceof Pit){
				if(Collision.boxCollide(box, next.getBox())){
					map.reset();
					return;
				}
			}
		}
	}
	
	private void handlePowerUse(boolean usePower){
		if(usePower){
			power.usePower();
		}
	}
	
	private byte lastDirection = NONE;
	private boolean directionWhileCollisionSet; // FALSE WHEN CAN MOVE
	
	private void changeTexture(byte direction){
	}
	
	private void handleTextureChange(){
//		if(nextPosCollision){
//			if(movingTo == UP){
//				texture = Texture.PLAYER_LOOK_TOP;
//			}
//			else if(movingTo == DOWN){
//				texture = Texture.PLAYER_LOOK_BOT;
//			}
//		}
		if(sideKeysPressed() && canMove && powered){
			if(lookingTo == LOOKING_TOP){
				if(texture == Texture.PLAYER_LOOK_TOP){
					setTexture(Texture.PLAYER_LOOK_TOP2);
				}
				else{
					setTexture(Texture.PLAYER_LOOK_TOP);
				}
			}
			else{
				if(texture == Texture.PLAYER_LOOK_BOT){
					setTexture(Texture.PLAYER_LOOK_BOT2);
				}
				else{
					setTexture(Texture.PLAYER_LOOK_BOT);
				}
			}
		}
		
		
		else if(upKeysPressed() && canMove && powered){
			if(texture == Texture.PLAYER_LOOK_TOP){
				setTexture(Texture.PLAYER_LOOK_TOP2);
			}
			else{
				setTexture(Texture.PLAYER_LOOK_TOP);
			}
		}
		
		else if(downKeysPressed() && canMove && powered){
			if(texture == Texture.PLAYER_LOOK_BOT){
				setTexture(Texture.PLAYER_LOOK_BOT2);
			}
			else{
				setTexture(Texture.PLAYER_LOOK_BOT);
			}
		}
	}
	
	private boolean sideKeysPressed(){
		return (leftKeysPressed() || rightKeysPressed());
	}
	
	private boolean leftKeysPressed(){
		return (Keys.isPressed(Keys.LEFT) || Keys.isPressed(Keys.KEY_Q) || Keys.isPressed(Keys.KEY_A));
	}
	
	private boolean rightKeysPressed(){
		return (Keys.isPressed(Keys.RIGHT) || Keys.isPressed(Keys.KEY_D));
	}
	
	private boolean upKeysPressed(){
		return (Keys.isPressed(Keys.UP) || Keys.isPressed(Keys.KEY_Z) || Keys.isPressed(Keys.KEY_W));
	}
	
	private boolean downKeysPressed(){
		return (Keys.isPressed(Keys.DOWN) || Keys.isPressed(Keys.KEY_S));
	}
	
	
	
	private void setNewCol(int offset){
		if(cptUpdateX > Map.TILE_SIZE - 1 || cptUpdateX == 0){
			if(movingTo == LEFT){
				x += 1;
			}
		}
		
		col += offset;
		resetColMoveData();
	}
	
	private void setNewRow(int offset){
		if(cptUpdateY > Map.TILE_SIZE - 1 || cptUpdateY == 0){
			if(movingTo == UP){
				y += 1;
			}
		}
		
		row += offset;
		resetRowMoveData();
	}
	
	private void resetColMoveData(){
		resetMovementData();
		cptUpdateX = 0;
	}
	
	private void resetRowMoveData(){
		resetMovementData();
		cptUpdateY = 0;
	}
	
	private void resetMovementData(){
		movingTo = NONE;
		movingToSet = false;
		canMove = true;
	}

	
	public void setTilePos(int row, int col){
		this.row = row;
		this.col = col;
		this.rowSave = row;
		this.colSave = col;
		x = col * Map.TILE_SIZE;
		y = row * Map.TILE_SIZE;
	}
	
	/**
	 * Graphics
	 */
	
	@Override
	public void render(Graphics2D g) {
		if(canRenderPower){
			power.render(g);
		}
		
		Renderer.basicRender(g, texture, x, y);
		renderingDeathScreen(g);
		
		renderProjectiles(g);
	}
	
	private void renderProjectiles(Graphics2D g){
		for(Projectile projectile : projectiles){
			projectile.render(g);
		}
	}
	
	private Font deathFont = new Font("Tahoma", Font.PLAIN, 9);
	private Color color = new Color(255, 216, 0);
	
	private void renderingDeathScreen(Graphics2D g){
		if(!canRenderDeathScreen){
			if(deathTimer != null){
				Logs.println("timer dead : " + (deathTimer.getTimeLeft() == 0 ? true : false)
						+ "canRender = " + canRenderDeathScreen);
			}
			return;
		}

		Font save = g.getFont();
		Color saveC = g.getColor();
		
		g.setFont(deathFont);
		g.setColor(color);
		
		Renderer.stringRender(g, "No more power !", Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 8);
		Renderer.stringRender(g, deathTimer.getTimeLeft() + "s", Game.WIDTH / 2, Game.HEIGHT / 2 + 1);
		
		g.setFont(save);
		g.setColor(saveC);
	}
	
	
	/**
	 * Tools
	 */
	
	private void setCurrentTile(){
		//currentTile = map.getTileAt(row, col);
	}
	
	public void setMap(Map map){
		this.map = map;
	}


	public List<Projectile> getProjectiles() {
		return projectiles;
	}
	

}
