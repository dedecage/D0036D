package model;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class handles all caching related operations in the program.
 * 
 */
public class Cache {

    /** The cached data. */
    private HashMap<String, NodeList> cachedData;
    
    /** The web API handler. */
    private WebApiHandler webApiHandler;


    /**
     * Instantiates a new cache.
     */
    public Cache() {
	cachedData = new HashMap<String, NodeList>();
	webApiHandler = new WebApiHandler();
    }

    /**
     * Gets the cached temperature.
     *
     * @param city the city
     * @param dateTime the date and time
     * @return the cached temperature
     * @throws Exception the exception "value not found"
     */
    private String getCachedTemperature(City city, String dateTime) throws Exception {
	String key = city.getName();
	NodeList cachedList = cachedData.get(key);
	return readTempFromData(cachedList, dateTime);
    }

    /**
     * Calls for fetching of new temperature data for a given city and returns the
     * value.
     *
     * @param city the city
     * @param dateTime the date and time
     * @return the new temperature
     * @throws Exception the exception "value not found"
     */
    private String getNewTemperature(City city, String dateTime) throws Exception {
	fetchAndStore(city);
	return getCachedTemperature(city, dateTime);
    }

    /**
     * Scans through a given NodeList and fetches the temperature for a given time
     * value.
     *
     * @param list the node list
     * @param dateTime the date and time
     * @return the value
     * @throws Exception the exception "value not found"
     */
    private String readTempFromData(NodeList list, String dateTime) throws Exception {
	for (int i = 0; i < list.getLength(); i++) {
	    Node n = list.item(i);
	    if (n.getNodeType() == Node.ELEMENT_NODE) {
		Element timeNode = (Element) n;
		String tFrom = timeNode.getAttribute("from");
		String tTo = timeNode.getAttribute("to");

		if (dateTime.equals(tFrom) && dateTime.equals(tTo)) {
		    NodeList dataList = timeNode.getElementsByTagName("temperature");
		    for (int j = 0; j < dataList.getLength(); j++) {
			Node n2 = (Node) dataList.item(j);
			if (n2.getNodeType() == Node.ELEMENT_NODE) {
			    Element temp = (Element) n2;
			    String value = temp.getAttribute("value");
			    return value;
			}
		    }
		}
	    }
	}
	throw new Exception("Value not found");
    }

    /**
     * Retrieves new data from online API and stores it in the cache map.
     *
     * @param city the city
     */
    private void fetchAndStore(City city) {
	NodeList list = webApiHandler.getWebData(city);
	String key = city.getName();
	cachedData.put(key, list);
    }

    /**
     * Removes the old data.
     *
     * @param key the key
     */
    private void removeOldData(String key) {
	cachedData.remove(key);
    }

    /**
     * Checks and manages the cache time for a given city before returning the
     * temperature value. This is the only method available to an external user.
     *
     * @param c the city
     * @param cacheTimeAdded the cache time added
     * @param dateTime the date and time
     * @return the value
     * @throws Exception the exception "value not found"
     */
    public String cacheControl(City c, long cacheTimeAdded, String dateTime) throws Exception {
	long currentTime = System.currentTimeMillis();
	long newCacheTime = currentTime + cacheTimeAdded;

	// If a new cacheTime is lower than the previous one. Since a new cache
	// time can't be negative, you don't need to check if it is outdated.
	if (newCacheTime <= c.getCacheTime()) {
	    c.updateCacheTime(newCacheTime);
	    return getCachedTemperature(c, dateTime);
	}
	// If the cacheTime has not yet expired, and the new cacheTime
	// is not lower than the previous one, let it expire first.
	if (!c.isOutdated(currentTime)) {
	    return getCachedTemperature(c, dateTime);
	}

	// If it is outdated.
	String key = c.getName();
	if (cachedData.containsKey(key)) {
	    removeOldData(key);
	}
	
	// If a completely new city is selected.
	c.updateCacheTime(newCacheTime);
	return getNewTemperature(c, dateTime);
    }
}

