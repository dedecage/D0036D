package model;


/**
 * This class enables us to turn cities into objects for
 * easier handling in the program.
 * 
 */
public class City {

    /** City information. */
    private String name, lat, lon;
    
    /** The cache time. */
    private long cacheTime;

    /**
     * Instantiates a new city.
     *
     * @param name the name
     * @param lat the latitude
     * @param lon the longitude
     */
    public City(String name, String lat, String lon) {
	this.name = name;
	this.lat = lat;
	this.lon = lon;
	cacheTime = System.currentTimeMillis();
    }
    

    /**
     * Gets the lat.
     *
     * @return the lat
     */
    public String getLat() {
	return this.lat;
    }
    
    /**
     * Gets the lon.
     *
     * @return the lon
     */
    public String getLon() {
	return this.lon;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the cache time.
     *
     * @return the cache time
     */
    public long getCacheTime() {
	return cacheTime;
    }

    /**
     * Update cache time.
     *
     * @param cacheTime the cache time
     */
    public void updateCacheTime(long cacheTime) {
	this.cacheTime = cacheTime;
    }
 
    /**
     * Checks if the cache time is outdated.
     *
     * @param currentTime the current time
     * @return true, if it is outdated
     */
    public boolean isOutdated(long currentTime) {
	return currentTime > cacheTime ? true : false;
    }
}
