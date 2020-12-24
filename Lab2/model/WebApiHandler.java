package model;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is dedicated to fetching data from the web API.
 * 
 */
public class WebApiHandler {

    /** The url string. */
    private String urlString;
    
    /** The url. */
    private URL url;
    
    /** The connection. */
    private HttpURLConnection connection;
    
    /** The time list. */
    private NodeList timeList;

    /**
     * Creates a URL string and URL object from the data in the city object.
     *
     * @param city the city
     */
    private void makeURL(City city) {
	urlString = "https://api.met.no/weatherapi/locationforecast/2.0/classic?lat=" + city.getLat()
	+ "&lon=" + city.getLon();
	try {
	    url = new URL(urlString);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    connection.setRequestProperty("User-Agent", "Chrome/4.0");
	    System.out.println("Ny data hämtad");
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} 
    }
    
    /**
     * Retrieves data from the web API. Returns the data in the form of a NodeList
     *
     * @param city the city
     * @return the web data
     */
    public NodeList getWebData(City city) {
	makeURL(city);
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	try {
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(connection.getInputStream());
	    timeList = doc.getElementsByTagName("time");
	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return timeList;
    }
    
}
