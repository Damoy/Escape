package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import entities.Player;
import entities.decorations.Decoration;
import entities.items.BasicDoorKey;
import entities.items.ExitDoorKey;
import entities.items.Key;
import entities.items.MapItem;
import entities.items.PowerBonus;
import entities.obstacles.BasicDoor;
import entities.obstacles.Bot;
import entities.obstacles.ClassicWall;
import entities.obstacles.DestructibleWall;
import entities.obstacles.Door;
import entities.obstacles.ExitDoor;
import entities.obstacles.Obstacle;
import entities.obstacles.Pit;
import gameTester.Game;
import graphics.Renderer;
import graphics.Texture;
import input.Keys;
import toolbox.Logs;
import toolbox.time.Timer;

public class Map {
	
	public final static int TILE_SIZE = 8;

	private List<Obstacle> obstacles;
	private List<Decoration> decorations;
	private List<MapItem> items;
	private Game game;
	
	private long lastTime;
	private long nowTime;
	private long lastTime2;
	private long nowTime2;
	
	private Tile tiles[][];
	private int numRowsTiles;
	private int numColsTiles;
	private int currentMinRow;
	private int currentMinCol;
	
	private int maxWidth;
	private int maxHeight;
	
	public final static byte LEVEL_0 = (byte) 0;
	public final static byte LEVEL_1 = (byte) 1;
	public final static byte LEVEL_2 = (byte) 2;
	public final static byte LEVEL_3 = (byte) 3;
	public final static byte LEVEL_4 = (byte) 4;
	public final static byte LEVEL_5 = (byte) 5;
	public final static byte LEVEL_6 = (byte) 6;
	public final static byte LEVEL_7 = (byte) 7;
	public final static byte LEVEL_8 = (byte) 8;
	public final static byte LEVEL_9 = (byte) 9;
	
	private byte level;
	
	private Player player;
	// private ExitDoorKey key;
	// private ExitDoor exitDoor;
	
	java.util.Map<Key, Door> doorData;
	
	
	public Map(Game game, Player player, byte level, int width, int height){
		obstacles = new ArrayList<>();
		items = new ArrayList<>();
		decorations = new ArrayList<>();
		doorData = new HashMap<>();
		exitDoor = null;
		
		this.game = game;
		// this.level = level;
		this.player = player;
		this.maxWidth = width;
		this.maxHeight = height;
		
		nowTime = System.currentTimeMillis();
		lastTime = nowTime;
		nowTime2 = System.currentTimeMillis();
		lastTime2 = nowTime;
		
		currentMinRow = 0;
		currentMinCol = 0;
		numRowsTiles = height / TILE_SIZE;
		numColsTiles = width / TILE_SIZE;
		
		tiles = new Tile[numRowsTiles][numColsTiles];
		
		for(int row = 0; row < numRowsTiles; row++){
			for(int col = 0; col < numColsTiles; col++){
				tiles[row][col] = new Tile(Tile.VISIBLE, row, col, Texture.GRAY_GROUND);
			}
		}
		
		
		setLevel(level);
		// genLevel();
	}
	
	public final static byte LEVEL_MAX = (byte) 9;
	
	public void checkReset(){
		if(Keys.isPressed(Keys.KEY_R)){
			switch(level){
				case LEVEL_0:
					reset();
					break;
				case LEVEL_1:
					reset();
					break;
				case LEVEL_2:
					reset();
					break;
				case LEVEL_3:
					reset();
					break;
				case LEVEL_4:
					reset();
					break;
				case LEVEL_5:
					reset();
					break;
				case LEVEL_6:
					reset();
					break;
				case LEVEL_7:
					reset();
					break;
				case LEVEL_8:
					reset();
					break;
				case LEVEL_9:
					reset();
					break;
			}
		}
	}
	
	public void reset(){
		player.reset();
		game.setMap(new Map(game, player, level, maxWidth, maxHeight));
	}
	
	
	private Timer endGameTimer;
	private boolean isEndGame;
	private final static byte FINAL_TIMER_DELAY = 10;
	
	public void endGame(){
		isEndGame = true;
		endGameTimer = new Timer(FINAL_TIMER_DELAY);
	}
	
	private void checkEndGame(){
		if(!isEndGame || endGameTimer == null) return;
		
		endGameTimer.increment();
		
		if(endGameTimer.getTimeLeft() <= 0){
			game.launchMenu();
		}
	}
	
	private boolean renderStartLevel;
	private Timer timerStartLevelTimer;
	private Font startLevelFont = new Font("Tahoma", Font.PLAIN, 9);
	private Color color = new Color(255, 255, 255);
	private Font endGameFont = new Font("Tahoma", Font.PLAIN, 10);
	
	public void setLevel(byte level){
		this.level = level;
		if(level != (byte) -1){
			renderStartLevel = true;
			timerStartLevelTimer = new Timer(3);
		}
		
		obstacles.clear();
		items.clear();
		decorations.clear();
		doorData.clear();
		genLevel();
	}
	
	public void updateTimerStartLevel(){
		if(!renderStartLevel || timerStartLevelTimer == null) return;
		
		timerStartLevelTimer.increment();
		
		if(timerStartLevelTimer.getTimeLeft() <= 0){
			renderStartLevel = false;
			timerStartLevelTimer = null;
		}
	}
	
	
	private ExitDoor exitDoor; // TODO CAN MAKE A REFERENCE AS THERE IS ONLY ONE EXIT DOOR
	
	public void genLevel(){
		player.setMap(this);
		decorations = new ArrayList<>();
		obstacles = new ArrayList<>();
		items = new ArrayList<>();
		doorData.clear();
		
		switch(level){
			case (byte) -1:
				player.setPos(Game.WIDTH / 2 - 4, Game.HEIGHT / 2 + 4);
				break;
			case LEVEL_0:
				// top left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				// top to bot left
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				
				// exit
				// tiles[0][1].setTex(Texture.DOOR);
				
				// top to right, top
				for(byte b = 2; b < numColsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				// top to right, bot
				for(byte b = 1; b < numColsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
				}
				
				// top right
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				
				
				// top right to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
				}
				
				// bot left
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				// bot right
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				// player start
				player.reset();
				player.setTilePos(8, 10);
				player.setPower(20);
				
				// door data
				ExitDoorKey exitDoorKey = new ExitDoorKey(6, 7, this);
				this.exitDoor = new ExitDoor(0, 1, this);
				doorData.put(exitDoorKey, exitDoor);
				obstacles.add(exitDoor);
				items.add(exitDoorKey);
				
				doorData.put(exitDoorKey, exitDoor);

				// power bonus
				PowerBonus powerBonus = new PowerBonus(2, 10, (byte) 5, this); // 5 is power bonus
				items.add(powerBonus);
				
				// walls in map
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 3));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 5));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 9));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 10));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 3));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 1));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 7));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 9));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 10));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 3));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 5));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 7));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 9));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 8, 4));
				
				break;
			case LEVEL_1:
				// TODO SEE IF DOUBLONS
				
				// top left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				// top right
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				// bot left
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				// bot right
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				// top to bot left
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				// top right to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
				}
				
				for(byte b = 1; b < numColsTiles - 2; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				// top to right, bot
				for(byte b = 1; b < numColsTiles - 1; b++){
					if(b == (byte) 8) continue;
					obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
				}
				
				// player start
				player.reset();
				player.setTilePos(1, 1);
				player.setPower(20);
// 				player.setPower(200);
				
				// map inside obstacles
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 5));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 1));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 2, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 2));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 3));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 6));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 7));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 1));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 3));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 4));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 5));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 6, 6));
				obstacles.add(new DestructibleWall(8, 8));

				
				BasicDoor bdoor1 = new BasicDoor(6, 2, this);
				obstacles.add(bdoor1);
				
				this.exitDoor = new ExitDoor(0, 10, this);
				obstacles.add(this.exitDoor);
				
				
				DestructibleWall dWall = new DestructibleWall(2, 7);
				obstacles.add(dWall);
				
				ExitDoorKey exitDoorKey1 = new ExitDoorKey(9, 8, this);
				BasicDoorKey basicDoorKey1 = new BasicDoorKey(1, 7, this);
				
				items.add(basicDoorKey1);
				items.add(exitDoorKey1);
				
				doorData.put(basicDoorKey1, bdoor1);
				doorData.put(exitDoorKey1, this.exitDoor);
				
				PowerBonus powerBonus1 = new PowerBonus(2, 5, (byte) 7, this);
				items.add(powerBonus1);
				break;
			case LEVEL_2:
				
				obstacles.add(new Pit(0, 4));
				obstacles.add(new Pit(1, 6));
				obstacles.add(new Pit(1, 7));
				obstacles.add(new Pit(2, 3));
				obstacles.add(new Pit(2, 0));
				obstacles.add(new Pit(2, 8));
				obstacles.add(new Pit(2, 10));
				obstacles.add(new Pit(2, 11));
				obstacles.add(new Pit(3, 8));
				obstacles.add(new Pit(4, 2));
				obstacles.add(new Pit(4, 4));
				obstacles.add(new Pit(4, 6));
				obstacles.add(new Pit(4, 7));
				obstacles.add(new Pit(4, 10));
				obstacles.add(new Pit(4, 11));
				obstacles.add(new Pit(5, 9));
				obstacles.add(new Pit(6, 1));
				obstacles.add(new Pit(6, 3));
				obstacles.add(new Pit(6, 5));
				obstacles.add(new Pit(6, 8));
				obstacles.add(new Pit(7, 7));
				obstacles.add(new Pit(6, 7));
				obstacles.add(new Pit(8, 2));
				obstacles.add(new Pit(8, 4));
				obstacles.add(new Pit(8, 6));
				obstacles.add(new Pit(9, 8));
				
				player.reset();
				player.setTilePos(0, numColsTiles - 2);
				player.setPower(6);
				
				ExitDoor exitDoor2 = new ExitDoor(9, 11, this);
				ExitDoorKey exitDoorKey2 = new ExitDoorKey(5, 5, this);
				obstacles.add(exitDoor2);
				items.add(exitDoorKey2);
				doorData.put(exitDoorKey2, exitDoor2);
				this.exitDoor = exitDoor2;
				
				PowerBonus powerBonus2 = new PowerBonus(3, 7, (byte) 10, this);
				items.add(powerBonus2);
				break;
			case LEVEL_3:
				
				// top left to right
				// bot left to right
				for(byte b = 1; b < numColsTiles - 1; b++){
					if(!(b == numColsTiles - 2)){
						obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
					}
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				
				// top right to bot
				// top left to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					if(!(b == numRowsTiles - 2)){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
					}
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				
				// top left
				// top right
				// bot left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				
				player.reset();
				player.setTilePos(numRowsTiles - 1, numColsTiles - 1);
				player.setPower(20);
				
				ExitDoor exitDoor3 = new ExitDoor(8, 0, this);
				ExitDoorKey exitDoorKey3 = new ExitDoorKey(5, 4, this);
				obstacles.add(exitDoor3);
				items.add(exitDoorKey3);
				doorData.put(exitDoorKey3, exitDoor3);
				this.exitDoor = exitDoor3;
				
				// ennemies
				obstacles.add(new Bot(player, this, 1, 10, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 1, 7, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 2, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 4, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 3, 8, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 5, 6, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 6, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 8, 2, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 8, 3, Bot.BotProjectile.GOING_RIGHT));
				
				PowerBonus powerBonus3 = new PowerBonus(1, 1, (byte) 10, this);
				items.add(powerBonus3);
				break;
			case LEVEL_4:
				
//				for(byte b = 1; b < numColsTiles - 1; b++){
//					if(!(b == numColsTiles - 2)){
//						obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
//					}
//					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
//				}
				
				player.reset();
				player.setTilePos(8, 0);
				player.setPower(11);
				
				ExitDoor exitDoor4 = new ExitDoor(0, 11, this);
				ExitDoorKey exitDoorKey4 = new ExitDoorKey(5, 11, this);
				obstacles.add(exitDoor4);
				items.add(exitDoorKey4);
				doorData.put(exitDoorKey4, exitDoor4);
				this.exitDoor = exitDoor4;
				
				obstacles.add(new Pit(2, 10));
				obstacles.add(new Pit(2, 11));
				obstacles.add(new Pit(3, 8));
				obstacles.add(new Pit(4, 2));
				obstacles.add(new Pit(4, 4));
				obstacles.add(new Pit(4, 6));
				obstacles.add(new Pit(4, 7));
				obstacles.add(new Pit(4, 10));
				obstacles.add(new Pit(4, 11));
				// obstacles.add(new Pit(5, 9));
				obstacles.add(new Pit(6, 1));
				obstacles.add(new Pit(6, 3));
				obstacles.add(new Pit(6, 5));
				obstacles.add(new Pit(6, 8));
				obstacles.add(new Pit(7, 7));
				obstacles.add(new Pit(6, 7));
				obstacles.add(new Pit(8, 2));
				
				obstacles.add(new Bot(player, this, 0, 1, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 0, 3, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 0, 5, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 0, 8, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 2, 0, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 5, 8, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 7, 6, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 8, 11, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 9, 11, Bot.BotProjectile.GOING_LEFT));
				
				PowerBonus powerBonus4 = new PowerBonus(3, 7, (byte) 6, this);
				items.add(powerBonus4);
				
				break;
			case LEVEL_5:
				
				for(byte b = 1; b < numColsTiles - 2; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
					//if(!(b == numColsTiles - 2)){
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				
				// top right to bot
				// top left to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					if(!(b == 1)){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
					}
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				
				// top left
				// bot left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				// bot right
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				player.reset();
				player.setTilePos(0, 11);
				player.setPower(20);
				
				obstacles.add(new Pit(2, 2));
				obstacles.add(new Pit(3, 5));
				obstacles.add(new Pit(4, 7));
				obstacles.add(new Pit(5, 9));
				obstacles.add(new Pit(6, 2));
				obstacles.add(new Pit(7, 5));
				
				// doors stuff
				ExitDoor exitDoor5 = new ExitDoor(9, 10, this);
				ExitDoorKey exitDoorKey5 = new ExitDoorKey(6, 1, this);
				BasicDoor bdoor5 = new BasicDoor(5, 8, this);
				BasicDoorKey bDoorKey5 = new BasicDoorKey(4, 1, this);
				
				obstacles.add(exitDoor5);
				obstacles.add(bdoor5);
				items.add(exitDoorKey5);
				items.add(bDoorKey5);
				doorData.put(exitDoorKey5, exitDoor5);
				doorData.put(bDoorKey5, bdoor5);
				this.exitDoor = exitDoor5;
				
				for(byte b = 1; b < 8 ; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, 5, b));
				}
				
				obstacles.add(new ClassicWall(Obstacle.CENTER, 5, 10));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 8));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 7, 9));
				
				PowerBonus powerBonus5 = new PowerBonus(6, 10, (byte) 10, this);
				items.add(powerBonus5);
				
				obstacles.add(new Bot(player, this, 3, 10, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 3, 4, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 4, 6, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 1, 1, Bot.BotProjectile.GOING_RIGHT));
				
				break;
			case LEVEL_6:
				for(byte b = 1; b < numColsTiles - 2; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				
				// top right to bot
				// top left to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				
				// right to exit door
				obstacles.add(new ClassicWall(Obstacle.CENTER, 0, 10));
				
				// top left
				// top right
				// bot left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				// bot right
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				player.reset();
				player.setTilePos(9, 10);
				player.setPower(20);
				
				for(byte b = 1; b < numColsTiles - 1; b++){

					if(b != 3){
						obstacles.add(new ClassicWall(Obstacle.CENTER, 3, b));
					}
					
					if(b != 2 && b != 6){
						obstacles.add(new ClassicWall(Obstacle.CENTER, 5, b));
					}
					
					if(b != 8 && b != 4){
						obstacles.add(new ClassicWall(Obstacle.CENTER, 7, b));
					}
				}
				
				ExitDoor exitDoor6 = new ExitDoor(0, 9, this);
				ExitDoorKey exitDoorKey6 = new ExitDoorKey(1, 1, this);
				obstacles.add(exitDoor6);
				items.add(exitDoorKey6);
				doorData.put(exitDoorKey6, exitDoor6);
				this.exitDoor = exitDoor6;
				
				obstacles.add(new Bot(player, this, 2, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 2, 10, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 4, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 4, 10, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 6, 10, Bot.BotProjectile.GOING_LEFT));
				obstacles.add(new Bot(player, this, 8, 1, Bot.BotProjectile.GOING_RIGHT));
				
				break;
			case LEVEL_7:
				for(byte b = 1; b < numColsTiles - 2; b++){
					if(b != 1){
						obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
					}
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				
				// top right to bot
				// top left to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
				}
				
				obstacles.add(new ClassicWall(Obstacle.CENTER, 9, 10));
				
				// top left
				// top right
				// bot left
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				// bot right
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				player.reset();
				player.setTilePos(0, 10);
				player.setPower(12);
				
				obstacles.add(new Bot(player, this, 1, 7, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 1, 9, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 2, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 5, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 6, 1, Bot.BotProjectile.GOING_RIGHT));
				obstacles.add(new Bot(player, this, 8, 7, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 8, 9, Bot.BotProjectile.GOING_UP));
				
				obstacles.add(new Pit(2, 8));
				obstacles.add(new Pit(3, 6));
				obstacles.add(new Pit(4, 8));
				obstacles.add(new Pit(6, 8));
				obstacles.add(new Pit(7, 6));
				
				for(byte b = 1; b < 5; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, 4, b));
				}
				
				PowerBonus powerBonus7 = new PowerBonus(1, 1, (byte) 13, this);
				items.add(powerBonus7);
				
				obstacles.add(new DestructibleWall(2, 2));
				obstacles.add(new DestructibleWall(2, 3));
				obstacles.add(new DestructibleWall(2, 4));
				obstacles.add(new DestructibleWall(8, 1));
				
				obstacles.add(new ClassicWall(Obstacle.CENTER, 3, 4));
				
				ExitDoor exitDoor7 = new ExitDoor(9, 1, this);
				ExitDoorKey exitDoorKey7 = new ExitDoorKey(3, 1, this);
				obstacles.add(exitDoor7);
				items.add(exitDoorKey7);
				doorData.put(exitDoorKey7, exitDoor7);
				this.exitDoor = exitDoor7;
				
				break;
			case LEVEL_8:
				
				for(byte b = 1; b < numColsTiles - 2; b++){
					if(b != 1){
						obstacles.add(new ClassicWall(Obstacle.CENTER, numRowsTiles - 1, b));
					}
					obstacles.add(new ClassicWall(Obstacle.CENTER, 0, b));
				}
				
				// top right to bot
				// top left to bot
				for(byte b = 1; b < numRowsTiles - 1; b++){
					obstacles.add(new ClassicWall(Obstacle.CENTER, b, numColsTiles - 1));
					if(b != 8){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 0));
					}
				}
				
				obstacles.add(new ClassicWall(Obstacle.CENTER, 0, 10));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 9, 10));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 9, 1));
				obstacles.add(new ClassicWall(Obstacle.TOP_LEFT, 0, 0));
				obstacles.add(new ClassicWall(Obstacle.TOP_RIGHT, 0, numColsTiles - 1));
				obstacles.add(new ClassicWall(Obstacle.BOT_LEFT, numRowsTiles - 1, 0));
				obstacles.add(new ClassicWall(Obstacle.BOT_RIGHT, numRowsTiles - 1, numColsTiles - 1));
				
				player.reset();
				player.setTilePos(8, 0);
				player.setPower(6);
				
				ExitDoor exitDoor8 = new ExitDoor(4, 11, this);
				ExitDoorKey exitDoorKey8 = new ExitDoorKey(1, 10, this);
				obstacles.add(exitDoor8);
				items.add(exitDoorKey8);
				doorData.put(exitDoorKey8, exitDoor8);
				this.exitDoor = exitDoor8;
				
				for(byte b = 1; b < numColsTiles - 1; b++){
					if(b != 6 && b != 3){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 2));
					}
					if(b != 2 && b!= 7){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 4));
					}
					if(b != 5){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 6));
					}
				}
				
				obstacles.add(new Bot(player, this, 1, 1, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 1, 5, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 1, 8, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 8, 3, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 8, 5, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 8, 7, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 8, 9, Bot.BotProjectile.GOING_UP));
				
				obstacles.add(new Pit(5, 8));
				
				PowerBonus powerBonus8 = new PowerBonus(3, 2, (byte) 12, this);
				items.add(powerBonus8);
				
				break;
			case LEVEL_9:
				
				BasicDoor basicDoor9_1 = new BasicDoor(4, 9, this);
				BasicDoor basicDoor9_2 = new BasicDoor(5, 5, this);
				BasicDoor basicDoor9_3 = new BasicDoor(6, 2, this);
				ExitDoor exitDoor9 = new ExitDoor(8, 0, this);
				
				BasicDoorKey basicDoorKey9_1 = new BasicDoorKey(9, 11, this);
				BasicDoorKey basicDoorKey9_2 = new BasicDoorKey(0, 6, this);
				BasicDoorKey basicDoorKey9_3 = new BasicDoorKey(9, 4, this);
				ExitDoorKey exitDoorKey9 = new ExitDoorKey(4, 0, this);
				
				this.exitDoor = exitDoor9;
				
				obstacles.add(basicDoor9_1);
				obstacles.add(basicDoor9_2);
				obstacles.add(basicDoor9_3);
				obstacles.add(exitDoor9);
				
				items.add(basicDoorKey9_1);
				items.add(basicDoorKey9_2);
				items.add(basicDoorKey9_3);
				items.add(exitDoorKey9);
				
				doorData.put(exitDoorKey9, exitDoor9);
				doorData.put(basicDoorKey9_1, basicDoor9_1);
				doorData.put(basicDoorKey9_2, basicDoor9_2);
				doorData.put(basicDoorKey9_3, basicDoor9_3);
				
				player.reset();
				player.setTilePos(0, 11);
				player.setPower(8);
				
				for(byte b = 0; b < numRowsTiles; b++){
					if(b != 4){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 9));
					}
				}
				
				obstacles.add(new ClassicWall(Obstacle.CENTER, 1, 11));
				obstacles.add(new ClassicWall(Obstacle.CENTER, 4, 11));
				
				obstacles.add(new Bot(player, this, 0, 1, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 0, 3, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 0, 8, Bot.BotProjectile.GOING_DOWN));
				obstacles.add(new Bot(player, this, 9, 1, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 9, 3, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 9, 6, Bot.BotProjectile.GOING_UP));
				obstacles.add(new Bot(player, this, 9, 10, Bot.BotProjectile.GOING_UP));
				
				PowerBonus powerBonus9 = new PowerBonus(5, 11, (byte) 6, this);
				items.add(powerBonus9);
				
				PowerBonus powerBonus10 = new PowerBonus(6, 7, (byte) 13, this);
				items.add(powerBonus10);
				
				PowerBonus powerBonus11 = new PowerBonus(4, 4, (byte) 11, this);
				items.add(powerBonus11);
				
				for(byte b = 0; b < numRowsTiles; b++){
					if(b != 6 && b != 2){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 7));
					}
				}
				
				for(byte b = 0; b < numRowsTiles; b++){
					if(b != 5){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 5));
					}
				}
				
				for(byte b = 0; b < numRowsTiles; b++){
					if(b != 6){
						obstacles.add(new ClassicWall(Obstacle.CENTER, b, 2));
					}
				}
				
				obstacles.add(new DestructibleWall(8, 4));
				
				for(byte b = 0; b < numRowsTiles; b++){
					if(b != 4 && b != 8){
						obstacles.add(new Pit(b, 0));
					}
				}
				
				
//				PowerBonus powerBonus12 = new PowerBonus(4, 0, (byte) 5, this);
//				items.add(powerBonus12);
				
				break;
		}
	}
	
	public void update(){
		updateTimerStartLevel();
		checkReset();
		updateObstacles();
		checkEndGame();
	}
	
	public List<PowerBonus> getPowerBonuss(){
		List<PowerBonus> allPowers = new ArrayList<>();
		Iterator<MapItem> iterator = items.iterator();
			
		while(iterator.hasNext()){
			MapItem item = iterator.next();
			
			if(item instanceof PowerBonus){
				allPowers.add((PowerBonus) item);
			}
		}
		return allPowers;
	}
	
	private void updateObstacles(){
		for(Obstacle o : obstacles){
			o.update();
		}
		for(MapItem m : items){
			m.update();
		}
		for(Decoration d : decorations){
			d.update();
		}
	}

	public Tile getTileAt(float x, float y){
		return tiles[(int) (y / TILE_SIZE)][(int) (x / TILE_SIZE)];
	}
	
	public Tile getTileAt(int row, int col){
		try{
			return tiles[row][col];
		}
		catch(Exception e){
			Player p = game.getPlayer();
			Logs.println("Bug, player row : " + p.getRow() + ", col : " + p.getCol() + ", max row : " + numRowsTiles
					+ ", max col : " + numColsTiles);
			throw e;
		}
	}
	
	
	public Obstacle getObstacleAt(float x, float y, int w, int h){
		if(x + w > maxWidth || y + h > maxHeight) return null;
		
		Tile t = getTileAt(x, y);
		return getObstacleAt(t.getRow(), t.getCol());
	}
	
	public Obstacle getObstacleAt(int row, int col){
		if(row > numRowsTiles || col > numColsTiles) return null;
		
		for(Obstacle o : obstacles){
			if(o.getRow() == row && o.getCol() == col){
				return o;
			}
		}
		return null;
	}
	
	public int getMaxHeight(){
		return maxHeight;
	}
	
	public int getMaxWidth(){
		return maxWidth;
	}
	
	
	public void render(Graphics2D g){
		renderTiles(g);
		renderObstacles(g);
		renderItems(g);
		renderDecorations(g);
		renderTimerStartLevel(g);
		renderEndGame(g);
	}
	
	public void renderTimerStartLevel(Graphics2D g){
		if(renderStartLevel && timerStartLevelTimer != null){
			Font save = g.getFont();
			Color saveC = g.getColor();
			
			g.setFont(startLevelFont);
			g.setColor(color);
			
			Renderer.stringRender(g, "Level " + level, Game.WIDTH / 2 - 16, Game.HEIGHT * 20/100);
			// Renderer.stringRender(g, timerStartLevelTimer.getTimeLeft() + "s", Game.WIDTH / 2, Game.HEIGHT / 2 + 1);
			
			g.setFont(save);
			g.setColor(saveC);
		}
	}
	
	private Color yellowColor = new Color(255, 214, 0);
	
	public void renderEndGame(Graphics2D g){
		if(isEndGame && endGameTimer != null){
			Font save = g.getFont();
			Color saveC = g.getColor();
			
			g.setFont(endGameFont);
			g.setColor(yellowColor);
			
			Renderer.stringRender(g, "Congratulations !", Game.WIDTH / 2 - 36, Game.HEIGHT / 2 - 12);
			Renderer.stringRender(g, "Thanks for playing !", Game.WIDTH / 2 - 44, Game.HEIGHT / 2);
			// Renderer.stringRender(g, endGameTimer.getTimeLeft() + "s", Game.WIDTH / 2 - 40, Game.HEIGHT / 2 + 12);
			Renderer.stringRender(g, endGameTimer.getTimeLeft() + "s", Game.WIDTH * 88/100, Game.HEIGHT * 95/100);
			
			g.setFont(save);
			g.setColor(saveC);
		}
	}
	
	private void renderTiles(Graphics2D g){
		for(int row = currentMinRow; row < numRowsTiles; row++){
			for(int col = currentMinCol; col < numColsTiles; col++){
				tiles[row][col].render(g);
			}
		}
	}
	
	private void renderObstacles(Graphics2D g){
		for(Obstacle o : obstacles){
			o.render(g);
		}
	}
	
	private void renderItems(Graphics2D g){
		for(MapItem m : items){
			m.render(g);
		}
	}
	
	private void renderDecorations(Graphics2D g){
		for(Decoration d : decorations){
			d.render(g);
		}
	}

	public List<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(List<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public Game getGame() {
		return game;
	}

	public long getLastTime() {
		return lastTime;
	}

	public long getNowTime() {
		return nowTime;
	}

	public long getLastTime2() {
		return lastTime2;
	}

	public long getNowTime2() {
		return nowTime2;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public int getNumRowsTiles() {
		return numRowsTiles;
	}

	public int getNumColsTiles() {
		return numColsTiles;
	}

	public int getCurrentMinRow() {
		return currentMinRow;
	}

	public int getCurrentMinCol() {
		return currentMinCol;
	}

	public byte getLevel() {
		return level;
	}

	public List<Decoration> getDecorations() {
		return decorations;
	}

	public List<MapItem> getItems() {
		return items;
	}

	public java.util.Map<Key, Door> getDoorData() {
		return doorData;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ExitDoor getExitDoor(){
		return exitDoor;
//		Iterator<Entry<Key, Door>> iterator = doorData.entrySet().iterator();
//		
//		while(iterator.hasNext()){
//			Entry<Key, Door> entry = iterator.next();
//			
//			Door door = entry.getValue();
//			
//			if(door instanceof ExitDoor){
//				return (ExitDoor) door;
//			}
//		}
//		
//		return null;
	}
	
	

}
