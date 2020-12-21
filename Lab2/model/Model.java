package model;
import java.util.ArrayList;

/**
 * This class is responsible for delegating data fetching through the caching system, 
 * and generating data for the GUI.
 * 
 */
public class Model {

    /** The XML reader. */
    private XMLReader xmlReader;
    
    /** The cities. */
    private ArrayList<City> cities;
    
    /** The cache. */
    private Cache cache;


    /**
     * Instantiates a new model.
     */
    public Model() {
	xmlReader = new XMLReader();
	cities = xmlReader.getList();
	cache = new Cache();
    }

    /**
     * Gets the temperature value through the caching system.
     *
     * @param city the city
     * @param addedCacheTime the added cache time
     * @param dateTime the date and time
     * @return the temperature
     * @throws Exception the exception "value not found"
     */
    public String getTemperature(City city, long addedCacheTime, String dateTime) throws Exception {
	return cache.cacheControl(city, addedCacheTime, dateTime);
    }

    /**
     * Generates and returns dates for the user to choose from.
     *
     * @return the dates
     */
    public String[] getDates() {
	String today = java.time.LocalDate.now().toString();
	String tomorrow = java.time.LocalDate.now().plusDays(1).toString();
	String[] dates = { today, tomorrow };
	return dates;
    }

    /**
     * Generates and returns times for the user to choose from
     *
     * @return the times
     */
    public String[] getTimes() {
	String[] times = { "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00",
		"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
		"21:00", "22:00", "23:00" };
	return times;
    }

    /**
     * Gets the cities.
     *
     * @return the cities
     */
    public String[] getCities() {
	String[] s = new String[cities.size()];
	for (int i = 0; i < s.length; i++) {
	    String cityName = cities.get(i).getName();
	    s[i] = cityName;
	}
	return s;
    }

    /**
     * Gets the city array list.
     *
     * @return the city array list
     */
    public ArrayList<City> getCityArrayList() {
	return cities;
    }
}
