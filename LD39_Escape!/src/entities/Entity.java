package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import physics.AABB;


public abstract class Entity {

	protected BufferedImage texture;
	
	protected float x;
	protected float y;
	
	protected int row;
	protected int col;
	
	protected AABB box;
	
	public Entity(BufferedImage texture, float initX, float initY){
		this.texture = texture;
		this.x = initX;
		this.y = initY;
		this.box = new AABB(this);
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g);

	
	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public AABB getBox(){
		return box;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPos(float x, float y){
		setX(x);
		setY(y);
	}
	
	public void incX(float x){
		this.x += x;
	}
	
	public void incY(float y){
		this.y += y;
	}
	
	public int getWidth(){
		return texture.getWidth();
	}
	
	public int getHeight(){
		return texture.getHeight();
	}
	
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(Object o){
		if(!(o instanceof Entity)){
			return false;
		}
		Entity e = (Entity) o;
		return (e.getX() == x && e.getY() == y);
	}
	
	public void updateBox(){
		box.update(x, y);
	}
	
	
	public static void updateBoxPos(Entity boxToUpdate, float x, float y){
		boxToUpdate.getBox().update(x, y);
	}
	
	public static void updateBoxPosX(Entity boxToUpdate, float x){
		boxToUpdate.getBox().updateX(x);
	}
	
	public static void updateBoxPosY(Entity boxToUpdate, float y){
		boxToUpdate.getBox().updateY(y);
	}
	
//	public static List<Entity> copy(List<Entity> toCopy, Entity... toAdd){
//		List<Entity> res = new ArrayList<>(toCopy);
//		for(Entity e : toAdd){
//			res.add(e);
//		}
//		return res;
//	}
//	
//	
//	public static List<Entity> checkEntityCollidingWith(Entity e, List<Entity> entities){
//		List<Entity> es = new ArrayList<>();
//		for(Entity eCheck : entities){
//			if(Collision.boxCollide(e.getBox(), eCheck.getBox())){
//				if(eCheck.equals(e)) continue;
//				es.add(eCheck);
//			}
//		}
//		return es;
//	}
	
}
