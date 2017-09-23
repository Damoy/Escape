package entities.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import graphics.Renderer;
import world.Map;

public abstract class MapItem extends Entity{

	protected Map map;
	
	public MapItem(BufferedImage texture, int row, int col, Map map) {
		super(texture, col * Map.TILE_SIZE, row * Map.TILE_SIZE);
		this.map = map;
	}
	
	@Override
	public void render(Graphics2D g){
		Renderer.basicRender(g, texture, x, y);
	}

}
