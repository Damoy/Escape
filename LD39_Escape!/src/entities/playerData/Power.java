package entities.playerData;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Player;
import graphics.Renderer;
import graphics.Texture;

public class Power {

	private int value;
	private int saveValue;
	private BufferedImage texture = Texture.POWER;
	private float xRender;
	private float yRender;
	
	private Font powerFont;
	private Player player;
	
	public Power(Player player, Font font, int value, float xStartRender, float yStartRender) {
		this.player = player;
		this.value = value;
		this.saveValue = value;
		this.xRender = xStartRender;
		this.yRender = yStartRender;
		this.powerFont = font;
	}
	
	public void usePower(){
		usePower(1);
	}
	
	public void growPower(int value){
		this.value += value;
	}
	
	public void setPower(int power){
		this.value = power;
		this.saveValue = power;
	}
	
	public void reset(){
		this.value = saveValue;
	}
	
	private boolean timeToUsePower = false;
	
	public void usePower(int value){
		if(timeToUsePower){
			this.value--;
			checkValue();
			timeToUsePower = false;
			return;
		}
		
		if(value == 1){
			timeToUsePower = true;
			return;
		}

		int val = value / 2;
		boolean left = value % 2 == 0 ? false : true;

		this.value -= val;
		if(left){
			timeToUsePower = true;
		}
	}
	
	private void checkValue(){
		if(value < 0){
			value = 0;
		}
	}
	
	public boolean noMorePower(){
		return value == 0;
	}
	
	public void update(){
		checkState();
	}
	
	private void checkState(){
		if(noMorePower()){
			player.hasNoMorePower();
		}
	}
	
	
	
	public void render(Graphics2D g){
		Renderer.basicRender(g, texture, xRender, yRender);
		
		Font save = g.getFont();
		g.setFont(powerFont);
		Renderer.stringRender(g, Integer.toString(value), xRender + 12, yRender + 7);
		g.setFont(save);
	}

	
	/**
	 * Getters
	 */
	
	public int getValue() {
		return value;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public float getxRender() {
		return xRender;
	}

	public float getyRender() {
		return yRender;
	}
	
	
	
	
}
