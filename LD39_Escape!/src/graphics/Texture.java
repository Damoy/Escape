package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	// player
	public final static BufferedImage PLAYER_LOOK_BOT = getPTexture(0, 40, 8, 8);
	public final static BufferedImage PLAYER_LOOK_BOT2 = getPTexture(0, 48, 8, 8);
	public final static BufferedImage PLAYER_LOOK_TOP = getPTexture(8, 40, 8, 8);
	public final static BufferedImage PLAYER_LOOK_TOP2 = getPTexture(8, 48, 8, 8);
	
	// obstacles
	public final static BufferedImage WALL1_TOP_LEFT = getPTexture(56, 16, 8, 8);
	public final static BufferedImage WALL1_CENTER = getPTexture(56, 24, 8, 8);
	public final static BufferedImage WALL1_TOP_RIGHT = getPTexture(64, 16, 8, 8);
	public final static BufferedImage WALL1_BOT_RIGHT = getPTexture(64, 24, 8, 8);
	public final static BufferedImage WALL1_BOT_LEFT = getPTexture(56, 32, 8, 8);
	
	public final static BufferedImage DESTRUCTIBLE_WALL_CENTER = getPTexture(48, 24, 8, 8);

	// objects and gui
	public final static BufferedImage POWER = getPTexture(0, 16, 8, 8);
	public final static BufferedImage POWER_COLLECT = getPTexture(8, 16, 8, 8);
	public final static BufferedImage GRAY_GROUND = getPTexture(16, 8, 8, 8);
	public final static BufferedImage EXIT_DOOR = getPTexture(32, 8, 8, 8);
	public final static BufferedImage EXIT_FREE = getPTexture(24, 8, 8, 8);
	public final static BufferedImage EXIT_DOOR_KEY = getPTexture(40, 17, 8, 6);
	public final static BufferedImage BASIC_DOOR_KEY = getPTexture(48, 16, 8, 8);
	
	public final static BufferedImage BASIC_DOOR = getPTexture(56, 0, 8, 8);
	
	public final static BufferedImage PLAYER_PROJECTILE = getPTexture(52, 35, 1, 1);
	public final static BufferedImage PIT = getPTexture(32, 0, 8, 8);
	
	public final static BufferedImage BOT_RIGHT = getPTexture(17, 48, 8, 8);
	public final static BufferedImage BOT_LEFT = getPTexture(25, 48, 8, 8);
	public final static BufferedImage BOT_DOWN = getPTexture(16, 56, 8, 8);
	public final static BufferedImage BOT_TOP = getPTexture(24, 56, 8, 8);
	
	public final static BufferedImage EXIT_DOOR_END = getPTexture(32, 16, 8, 8);
	
	protected static BufferedImage getPTexture(int xp, int yp, int width, int height){
		BufferedImage tileImage;
		try {
			tileImage = ImageIO.read(new FileInputStream("./resources/res_data.png"));
			tileImage = tileImage.getSubimage(xp, yp, width, height);
			
			int w = tileImage.getWidth();
			int h = tileImage.getHeight();
			
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
			         int argb = tileImage.getRGB(x, y);
			         if (argb == Color.MAGENTA.getRGB()){
			              tileImage.setRGB(x, y, 0);
			         }
			    }
			}
			return tileImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
