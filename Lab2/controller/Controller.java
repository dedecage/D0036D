package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.City;
import model.Model;

import view.View;

/**
 * This class binds together the View and Model class of the program by making the model reactive to the 
 * view, and transforms the entered cache time to a suitable format. 
 * 
 */
public class Controller {

    
    /** The model. */
    private Model model;
    
    /** The view. */
    private View view;
    
    /** Cache box data. */
    private String[] cities, dates, times;
    
    /** Relevant buttons. */
    private JButton goButton, returnButton, updateButton;
    
    /** Combo boxes. */
    private JComboBox<String> cityBox, dateBox, timeBox, cacheTimeBox;
    
    /** The city array list. */
    private ArrayList<City> cityArrayList;
    
    /** The latest city. */
    private City latestCity;
    
    /** The latest cache time added. */
    private long latestCacheTimeAdded;
    
    /** The latest date time. */
    private String latestDateTime, latestTime;

    /**
     * Initializer for the controller class.
     */
    public void Initialize() {
	
	model = new Model();
	cities = model.getCities();
	times = model.getTimes();
	dates = model.getDates();
	cityArrayList = model.getCityArrayList();
	
	view = new View(cities, dates, times);
	cityBox = view.getCityBox();
	dateBox = view.getDateBox();
	timeBox = view.getTimeBox();
	cacheTimeBox = view.getCacheTimeBox();
	goButton = view.getGoButton();
	returnButton = view.getReturnButton();
	updateButton = view.getUpdateButton();
	
	goButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		int cityIndex = cityBox.getSelectedIndex();
		City c = cityArrayList.get(cityIndex);
		String date = dateBox.getSelectedItem().toString();
		String time = timeBox.getSelectedItem().toString();
		String dateTime = date + "T" + time + ":00Z";
		
		int cacheIndex = cacheTimeBox.getSelectedIndex();
		long cacheTimeAdded = makeCacheTime(cacheIndex);
		
		try {
		    latestData(c, cacheTimeAdded, dateTime, time);
		    String temperature = model.getTemperature(c, cacheTimeAdded, dateTime);
		    view.switchToDisplay(c.getName(), temperature, time);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    JOptionPane.showMessageDialog(new JFrame(), "Värde för denna tid kunde inte hittas, "
		    	+ "vänligen prova en annan tidpunkt");
		}
	    }
	    
	});
	
	returnButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		view.switchToStartingFrame();
	    }
	    
	});
	
	updateButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		try {
		    String temperature = model.getTemperature(latestCity, latestCacheTimeAdded, latestDateTime);
		    String latestCityName = latestCity.getName();
		    view.update(latestCityName, temperature, latestTime);
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		
	    }
	    
	});
    }
    
    /**
     * Saves the latest parameters chosen by the user for updating purposes.
     *
     * @param c the city
     * @param cacheTimeAdded the cache time added
     * @param dateTime date and time 
     * @param time the time
     */
    private void latestData(City c, long cacheTimeAdded, String dateTime, String time) {
	latestCity = c;
	latestCacheTimeAdded = cacheTimeAdded;
	latestDateTime = dateTime;
	latestTime = time;
    }
    
    /**
     * Transforms the minutes set by the user to a suitable time format.
     *
     * @param cacheIndex the cache index
     * @return the time added in milliseconds
     */
    private long makeCacheTime(int cacheIndex) {
	int minutes = indexToCacheTime(cacheIndex);
	long timeAdded = minutes * 60000;
	return timeAdded;
    }
    
    /**
     * Index to cache time in minutes.
     *
     * @param index the index
     * @return minutes
     */
    private int indexToCacheTime(int index) {
	if (index == 0) {
	    return 1;
	} else if (index == 1) {
	    return 5;
	} 
	return 10;
    }
}
