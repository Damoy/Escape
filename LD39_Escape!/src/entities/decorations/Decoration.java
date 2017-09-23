package entities.decorations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import graphics.Renderer;

public abstract class Decoration extends Entity{

	public Decoration(BufferedImage texture, float initX, float initY) {
		super(texture, initX, initY);
	}

	public abstract void update();
	
	@Override
	public void render(Graphics2D g) {
		Renderer.basicRender(g, texture, x, y);
	}

}
