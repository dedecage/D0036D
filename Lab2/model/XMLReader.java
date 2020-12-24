package model;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles parsing of the local XML-file and creates a list
 * of city objects based on the parsed information.
 * 
 */
public class XMLReader {


	/** The cities. */
	private ArrayList<City> cities;
	
	/**
	 * Instantiates a new XML reader.
	 */
	public XMLReader() {
		cities = new ArrayList<City>();
	}

	/**
	 * Parses the local XML-file.
	 */
	private void read() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("places.xml");
			NodeList list = doc.getElementsByTagName("locality");
			for(int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element locality = (Element) n;
					String name = locality.getAttribute("name");
					NodeList data = locality.getChildNodes();
					for (int j = 0; j < data.getLength(); j++) {
						Node n2 = data.item(j);
						if (n2.getNodeType() == Node.ELEMENT_NODE) {
							Element attributes = (Element) n2;
							String lat = attributes.getAttribute("latitude");
							String lon = attributes.getAttribute("longitude");
							City c = new City(name, lat, lon);
							cities.add(c);
						}
					}
				} 
			}
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
	}
	
	/**
	 * Returns an ArrayList of city objects.
	 *
	 * @return the list
	 */
	public ArrayList<City> getList() {
	    read();
	    return cities;
	}
	
}
