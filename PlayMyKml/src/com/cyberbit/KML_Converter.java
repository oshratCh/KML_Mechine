package com.cyberbit;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class KML_Converter {
	
	public List<Map<String, String>> parseFileData(String pathname) {
		
		List<Map<String, String>> sections = new ArrayList<Map<String, String>>();
		Map<String, String> sectionContent = new HashMap<String, String>();
		NodeList timeNode = getKmlPoints( pathname, "when");
		NodeList coordNode = getKmlPoints( pathname, "gx:coord");
		for (int i = 0; i < coordNode.getLength(); i++) {
			sectionContent.put("DateTime", timeNode.item(i).getTextContent());
			sectionContent.put("Coord", coordNode.item(i).getTextContent());
			sections.add(sectionContent);
			
		}
		
		return sections;
	}
	
	protected NodeList getKmlPoints( String pathname, String tagName) {
		try{
			
			File fXmlFile = new File(pathname);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
			NodeList documant = doc.getElementsByTagName("Document");
			
			Node nNode = documant.item(0);
			Element eElement = (Element) nNode;
			
			Element folder =  (Element) eElement.getElementsByTagName("Folder").item(0);	
			Element placemark =  (Element) folder.getElementsByTagName("Placemark").item(2);
			Element track =  (Element) placemark.getElementsByTagName("gx:Track").item(0);
			NodeList nList = placemark.getElementsByTagName(tagName);
			return nList;
		}
		 catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
    }
}
