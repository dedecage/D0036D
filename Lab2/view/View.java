package view;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 * This class handles all GUI-related features of the program.
 * 
 */
public class View {

    /** Cache times. */
    private String[] cacheTider = { "1 minut", "5 minuter", "10 minuter" };
    
    /** The update button. */
    private JButton goButton, returnButton, updateButton;
    
    /** Combo boxes. */
    private JComboBox<String> cityBox, timeBox, dateBox, cacheTimeBox;
    
    /** The label. */
    private Label label;
    
    /** The frames. */
    private JFrame f1, f2;

    /** The current hour. */
    private int currentHour;

    /**
     * Instantiates a new view.
     *
     * @param städer the cities
     * @param datum the dates
     * @param tider the times
     */
    public View(String[] städer, String[] datum, String[] tider) {
	
	goButton = new JButton("Visa temperatur");
	
	cityBox = new JComboBox<String>(städer);
	dateBox = new JComboBox<String>(datum);
	
	timeBox = new JComboBox<String>(tider);
	currentHour = java.time.LocalTime.now().getHour();
	timeBox.setSelectedIndex(currentHour);
	
	cacheTimeBox = new JComboBox<String>(cacheTider);
	cacheTimeBox.setSelectedIndex(2);
	
	label = new Label();

	// Starting frame
	f1 = new JFrame("Väderapplikation");
	f1.pack();
	f1.setSize(300, 300);
	f1.setLocation(300, 200);
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f1.setLayout(new GridLayout(9, 1));
	f1.add(new Label("Stad"));
	f1.add(cityBox);
	f1.add(new Label("Datum"));
	f1.add(dateBox);
	f1.add(new Label("Tid"));
	f1.add(timeBox);
	f1.add(new Label("Hämta ny data efter"));
	f1.add(cacheTimeBox);
	f1.add(goButton);
	f1.setVisible(true);
	
	// Display frame
	f2 = new JFrame("Väderapplikation");
	returnButton = new JButton("Återvänd till meny");
	updateButton = new JButton("Uppdatera");
	f2.pack();
	f2.setSize(600, 100);
	f2.setLocation(300,200);
	f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f2.setLayout(new GridLayout(2, 2));
	f2.add(BorderLayout.NORTH, label);
	f2.add(new Label(" "));
	f2.add(returnButton);
	f2.add(updateButton);
	

    }

    /**
     * Gets the go button.
     *
     * @return the go button
     */
    public JButton getGoButton() {
	return goButton;
    }

    /**
     * Gets the city box.
     *
     * @return the city box
     */
    public JComboBox<String> getCityBox() {
	return cityBox;
    }


    /**
     * Gets the date box.
     *
     * @return the date box
     */
    public JComboBox<String> getDateBox() {
	return dateBox;
    }

    /**
     * Gets the time box.
     *
     * @return the time box
     */
    public JComboBox<String> getTimeBox() {
	return timeBox;
    }

    /**
     * Gets the cache time box.
     *
     * @return the cache time box
     */
    public JComboBox<String> getCacheTimeBox() {
	return cacheTimeBox;
    }

    /**
     * Gets the return button.
     *
     * @return the return button
     */
    public JButton getReturnButton() {
	return returnButton;
    }

    /**
     * Gets the update button.
     *
     * @return the update button
     */
    public JButton getUpdateButton() {
	return updateButton;
    }
    
    /**
     * Switch to display.
     *
     * @param cityName the city name
     * @param degrees the degrees
     * @param time the time
     */
    public void switchToDisplay(String cityName, String degrees, String time) {
	f1.setVisible(false);
	label.setText("Temperatur i " + cityName + " klockan " + time + ": " + degrees + "°C");
	f2.setVisible(true);
    }

    /**
     * Switch to starting frame.
     */
    public void switchToStartingFrame() {
	f2.setVisible(false);
	f1.setVisible(true);
    }

    /**
     * Update.
     *
     * @param cityName the city name
     * @param degrees the degrees
     * @param time the time
     */
    public void update(String cityName, String degrees, String time) {
	label.setText("Temperatur i " + cityName + " klockan " + time + ": " + degrees + "°C");
    }

}
