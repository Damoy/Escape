package gameTester;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import entities.Player;
import entities.obstacles.Obstacle;
import graphics.Renderer;
import graphics.Window;
import input.Keys;
import sound.Sound;
import toolbox.time.TimeCounter;
import world.Map;


public class Game extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = -3978669110693639000L;
	
	// dimensions
//	public static final int HEIGHT = 120; // 120 // 240
//	public static final int WIDTH = 160; // 160 // 320
//	private static final int SCALE = 4;
	public static final int HEIGHT = 80; // 120 // 240
	public static final int WIDTH = 96; // 160 // 320
	private static final int SCALE = 5;
	
	private static final int S_WIDTH = WIDTH * SCALE;
	private static final int S_HEIGHT = HEIGHT * SCALE;
	
	// game name
	public final static String GAME_TITLE = "Escape !";

	// game thread
	private Thread thread;
	private boolean running;
	
	// screen and image
	private BufferedImage image;
	private Graphics2D g;
	
	// game stuff
	private Window window;
	private Player player;
	private TimeCounter timeCounter;
	private Map map;
	private List<Obstacle> obstacles;
	
	
	public Game(){
		super();
		setComponent();
		init();
	}
	
	private void setComponent(){
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	
	private void init(){
		// JukeBox.init();
		running = false;
		this.window = new Window(GAME_TITLE, this);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		timeCounter = new TimeCounter();
		initMenu();
	}
	
	public final static byte MENU_STATE = (byte) -1;
	public final static byte IN_GAME_STATE = (byte) 1;
	public final static byte HELP_MENU_STATE = (byte) 2;
	private byte state;
	
	private void initMenu(){
		state = MENU_STATE;
	}
	
	private void initGame(){
		player = new Player(this, 2, 2);
		//map = new Map(this, player, Map.LEVEL_0, WIDTH, HEIGHT);
		map = new Map(this, player, Map.LEVEL_0, WIDTH, HEIGHT);
		obstacles = map.getObstacles();
	}
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		while (running) {
			if(timeCounter != null){
				timeCounter.increment();
			}
			
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				update();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
				renderToScreen();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				window.setTitle(frames + " fps");
				// ticks + " ticks, " 
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void start(){
		if(running) return;
		running = true;
		window.start();
	}
	
	public void stop(){
		if(!running) return;
		running = false;
	}

	// menu stuff
	private boolean isInMenu;
	private Font menuFont = new Font("Tahoma", Font.PLAIN, 12);
	private Font menuLittleFont10 = new Font("Tahoma", Font.PLAIN, 10);
	private Font menuLittleFont9 = new Font("Tahoma", Font.PLAIN, 9); // LINUX SUPPORT
	private Font menuLittleFont4 = new Font("Tahoma", Font.PLAIN, 8);
	private byte selected;
	
	private void checkSelectedMenu(){
		boolean should = false;
		if(Keys.isPressed(Keys.DOWN)){
			should = true;
			selected++;
		}
		if(Keys.isPressed(Keys.UP)){
			should = true;
			selected--;
		}
		
		if(selected < 0){
			should = false;
			selected = 0;
		}
		
		if(selected > 1){
			should = false;
			selected = 1;
		}
		
		if(should){
			Sound.menuOption.play();
			// JukeBox.playMenuOption();
		}
	}
	
	public void render(){
		if(state == MENU_STATE){
			Color c = g.getColor();
			
			renderBlackScreen();
			
			Font fontSave = g.getFont();
			g.setFont(menuFont);
			g.setColor(Color.WHITE);
			
			Renderer.stringRender(g, "Escape !", Game.WIDTH / 2 - 20 - 4, Game.HEIGHT / 3 - 8); // linux - 4
			
			g.setFont(menuLittleFont10);
			
			Renderer.stringRender(g, "Play", Game.WIDTH / 2 - 8 - 4, Game.HEIGHT / 2);
			Renderer.stringRender(g, "Help", Game.WIDTH / 2 - 8 - 4, Game.HEIGHT / 2 + 16);
			
			if(selected == 0){
				Renderer.stringRender(g, ">> ", Game.WIDTH / 2 - 32 - 4, Game.HEIGHT / 2);
			}
			else if(selected == 1){
				Renderer.stringRender(g, ">> ", Game.WIDTH / 2 - 32 - 4, Game.HEIGHT / 2 + 16);
			}
			g.setFont(menuLittleFont4);
			Renderer.stringRender(g, "By Damoy", Game.WIDTH * 50/100, Game.HEIGHT * 95 / 100);
			
			g.setColor(c);
			g.setFont(fontSave);
			
		}
		else if(state == HELP_MENU_STATE){
			Color c = g.getColor();
			
			renderBlackScreen();
			
			Font fontSave = g.getFont();
			g.setFont(menuFont);
			g.setColor(Color.WHITE);
			
			Renderer.stringRender(g, "Help", Game.WIDTH / 2 - 8, Game.HEIGHT / 3 - 12);
			
			//g.setFont(menuLittleFont10);
			g.setFont(menuLittleFont9);
			
			Renderer.stringRender(g, "Escape !", Game.WIDTH / 2 - 45, Game.HEIGHT * 35/100);
			Renderer.stringRender(g, "Power limited !", Game.WIDTH / 2 - 45, Game.HEIGHT * 50/100);
			
			Renderer.stringRender(g, "R: reset map", Game.WIDTH / 2 - 45, Game.HEIGHT * 65 / 100);
			Renderer.stringRender(g, "Arrows: movement", Game.WIDTH / 2 - 45, Game.HEIGHT * 80/100);
			Renderer.stringRender(g, "SPACE: shoot", Game.WIDTH / 2 - 45, Game.HEIGHT * 95 / 100);
			
//			g.setFont(menuLittleFont4);
//			Renderer.stringRender(g, "^", Game.WIDTH * 80 / 100, Game.HEIGHT * 93 / 100);
//			Renderer.stringRender(g, "|", Game.WIDTH * 8 / 100, Game.HEIGHT * 97 / 100);
//			
//			Renderer.stringRender(g, "|", Game.WIDTH * 95 / 100 + 5, Game.HEIGHT * 95 / 100);
//			
//			AffineTransform saveTransform = g.getTransform();
//			g.rotate(Math.toRadians(180));
//			Renderer.stringRender(g, "^", Game.WIDTH * 95 / 100 + 5, Game.HEIGHT * 97 / 100);
//			
//			g.setTransform(saveTransform);
			g.setColor(c);
			g.setFont(fontSave);
	}
	else if(state == IN_GAME_STATE){
			renderWhiteScreen();
			map.render(g);
			player.render(g);
		}
	}
	
	private void renderWhiteScreen(){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	private void renderBlackScreen(){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	public void renderToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, S_WIDTH, S_HEIGHT, null);
		g2.dispose();
	}
	
	public void update(){
		if(state == MENU_STATE){
			checkMenuInput();
			checkSelectedMenu();
		}
		else if(state == HELP_MENU_STATE){
			if(Keys.isPressed(Keys.ESCAPE)){
				Sound.menuSelect.play();
				//JukeBox.playMenuSelection();
				state = MENU_STATE;
			}
		}
		else if(state == IN_GAME_STATE){
			map.update();
			player.update();
		}
	}
	
	private void checkMenuInput(){
		if(Keys.isPressed(Keys.ENTER)){
			
			Sound.menuSelect.play();
			//JukeBox.playMenuSelection();
			
			if(selected == (byte) 0){
				state = IN_GAME_STATE;
				initGame();
			}
			else if(selected == (byte) 1){
				state = HELP_MENU_STATE;
			}
		}
//		else if(Keys.isPressed(Keys.ESCAPE)){
//			System.exit(0);
//		}
	}
	
	
	public void launchMenu(){
		state = MENU_STATE;
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Keys.keySet(e.getKeyCode(), true);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(state == MENU_STATE || state == HELP_MENU_STATE){
			//return;
//			if(e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER
//					|| e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP){
//				
//			}
		}
		Keys.keySet(e.getKeyCode(), false);
	}
	
	public boolean isRunning(){
		return running;
	}
	

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Graphics2D getG() {
		return g;
	}

	public void setG(Graphics2D g) {
		this.g = g;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public TimeCounter getTimeCounter() {
		return timeCounter;
	}

	public void setTimeCounter(TimeCounter timeCounter) {
		this.timeCounter = timeCounter;
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public List<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public void setObstacles(List<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public static void main(String[] args){
		Game game = new Game();
		game.start();
	}

	
}
