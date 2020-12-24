package server;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import server.GridPanel;

/**
 * The main class for the GUI. This class attaches the grid-panel to the GUI and acts
 * as an observer to the Server-class in order to properly update the GUI whenever the
 * client sends another package.
 * 
 */
public class View extends JFrame implements Observer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1506566013164686286L;
    
    /** The grid panel. */
    private GridPanel gp;

    /**
     * Update.
     *
     * @param arg0 the arg 0
     * @param arg1 the packet sent from the client.
     */
    @Override
    public void update(Observable arg0, Object arg1) {
	// TODO Auto-generated method stub
	gp.drawSquare((int[]) arg1);
    }
    
    /**
     * Instantiates a new view.
     */
    public View() {
	super("SpelGUI");
	setSize(new Dimension(1920, 1080));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	JPanel FixedPanel = new JPanel(new GridBagLayout());
	FixedPanel.setPreferredSize(this.getSize());
	
	gp = new GridPanel();
	gp.setPreferredSize(new Dimension(1609, 805));
	
	FixedPanel.add(gp);
	add(FixedPanel);

        setVisible(true);
    } 
}
