package world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Renderer;

/**
 * (Taken from my android university project and updated)
 */

/**
 * The tile class,
 * a tile is an element of the map,
 * it has two states visible and invisible.
 * Since the generation of the map all the tiles
 * already exist but they are invisible to the user.
 */
public class Tile{

	public final static byte VISIBLE = (byte) 1;
	public final static byte INVISIBLE = (byte) 2;
	
	private byte state;
    // the sizes and position
    private int width, height, row, col;
    // the texture
    private BufferedImage texture;

    /**
     * Generates new tile
     * @param state the init state
     * @param row the tile row
     * @param col the tile col
     * @param textureContext the texture context
     * @param texture the texture
     */
    public Tile(byte state, int row, int col, BufferedImage texture){
        width = texture.getWidth();
        height = texture.getHeight();
        this.state = state;
        this.row = row;
        this.col = col;
        this.texture = texture;
    }


    /**
     * Render the tile
     */
    public void render(Graphics2D g){
        if(state == INVISIBLE){
            return;
        }
        Renderer.basicRender(g, texture, width * col, height * row);
    }


    /**
     * Make the tile visible
     */
    public void makeVisible(){
    	state = VISIBLE;
    }

    /**
     * Make the tile visible
     */
    public void makeInvisible(){
    	state = INVISIBLE;
    }

    /**
     * Is it visible
     */
    public boolean isVisible(){
        return state == VISIBLE;
    }

    /**
     * Is it invisible
     */
    public boolean isInvisible(){
        return state == INVISIBLE;
    }


    /**
     * Getters setters
     */
    public void setTex(BufferedImage texture){this.texture = texture;}

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSquared(){
        return width == height;
    }

    public BufferedImage getTexture(){
        return texture;
    }

    public byte getState(){return state;}

    public void setVisibility(byte tileState){this.state = tileState;}

    public String toString(){
        return "row: " + row + ", col: " + col + ", state: " + state;
    }

    
    


}
