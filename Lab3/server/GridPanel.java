package server;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * GridPanel paints the game grid, in addition to holding the logistics 
 * for player positions in a 2d array.
 * 
 */
public class GridPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3728396685715564240L;
    
    /** The x pixel size. */
    private int xSize = 8;
    /** The y pixel size. */
    private int ySize = 4;

    /** The grid represented in a 2d array. */
    private Entity[][] grid = new Entity[201][201];
    
    
    
    /**
     * Paint component.
     *
     * @param g Graphics
     */
    public void paintComponent(Graphics g) {
	
	g.setColor(Color.BLACK);
	g.fillRect(0, 0, 1608, 804);
	  
        for (int i = 0; i < 201; i++) {
            for (int j = 0; j < 201; j++) {
        	if (grid[i][j] != null) {
        	    Entity e = grid[i][j];
        	    int entityX = e.getX();
        	    int entityY = e.getY();
        	    int entityColor = e.getColor();
        	    Color c = getColor(entityColor);
        	    g.setColor(c);
        	    g.fillRect(entityX * xSize, entityY * ySize, xSize, ySize);
        	}
            }
        }
    }
    
	/**
	 * Draw square.
	 *
	 * @param ints the ints
	 */
	public void drawSquare(int[] ints) {
		int x = ints[0];
		int y = ints[1];
		int color = ints[2];
		Entity e = new Entity(x, y, color);
		try {
			grid[x][y] = e;
		} catch (ArrayIndexOutOfBoundsException a) {
			System.out.println("koordinat utanför spelplan");
		}
		repaint();
	}
	
	/**
	 * Takes the integer from an Entity object and returns
	 * a corresponding Color object.
	 * @param i the color integer of the Entity object 
	 * @return The converted Color object
	 */
	public Color getColor(int i) {
    	Color col = Color.BLACK;
    	    switch (i) {
    	    case 1:
    	        col = Color.WHITE;
    	        break;
    	    case 2:
    	        col = Color.BLUE;
    	        break;
    	    case 3:
    	        col = Color.GRAY;
    	        break;
    	    case 4:
    	        col = Color.GREEN;
    	        break;
    	    case 5:
    	        col = Color.YELLOW;
    	        break;
    	    case 6:
    	        col = Color.ORANGE;
    	        break;
    	    case 7:
    	        col = Color.PINK;
    	        break;
    	    case 8:
    	        col = Color.RED;
    	        break;
    	        }
    	    return col;
    	    }
    
}
