package server;

/**
 * An Entity represents a pixel on the game board. 
 * 
 */
public class Entity {
	
	
	/** The colors. */
	private int x, y, color;
	
	
	/**
	 * Instantiates a new entity.
	 *
	 * @param x the x
	 * @param y the y
	 * @param color the color
	 */
	public Entity(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public int getColor() {
		return color;
	}
}
