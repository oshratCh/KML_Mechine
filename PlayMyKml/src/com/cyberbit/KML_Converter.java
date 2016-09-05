package com.cyberbit;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
			sectionContent.put("SERVICE_NAME", "reference");
			if(timeNode.item(i) != null)
				sectionContent.put("INTERCEPTION_INFO_INTERCEPTION_TIME", timeNode.item(i).getTextContent());
			else
				sectionContent.put("INTERCEPTION_INFO_INTERCEPTION_TIME", "00:00 No time");
			String Coords = coordNode.item(i).getTextContent();
			sectionContent.put("MAIL_FROM_LOCATION_LONGITUDE", Coords.split(" ")[0]);
			sectionContent.put("MAIL_FROM_LOCATION_LATITUDE", Coords.split(" ")[1]);
			addReferenceDescription(sectionContent);
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
			NodeList placemarks = folder.getElementsByTagName("Placemark");
			for (int i = 0; i < placemarks.getLength(); i++) {
				Element placemark =  (Element) folder.getElementsByTagName("Placemark").item(i);
				Element track =  (Element) placemark.getElementsByTagName("gx:Track").item(0);
				if(track != null){
					NodeList nList = placemark.getElementsByTagName(tagName);
					return nList;
				}
			}
			return null;
		}
		 catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
    }

	protected void addReferenceDescription(Map<String, String>  aPlacemark){
		StringBuffer descriptionBuff = new StringBuffer("<![CDATA[");
        String time = aPlacemark.get("INTERCEPTION_INFO_INTERCEPTION_TIME");
        descriptionBuff.append("<h3>").append(" Date: ").append("<span style='font-weight:normal'>").append(time).append("</span>").append("</h3>");
        
        String coord_long = aPlacemark.get("MAIL_FROM_LOCATION_LONGITUDE");
        String coord_lat = aPlacemark.get("MAIL_FROM_LOCATION_LATITUDE");
        descriptionBuff.append("<h3>").append(" Location: ").append("<span style='font-weight:normal'>").append(coord_long+" , "+ coord_lat).append("</span>").append("</h3>");
        aPlacemark.put("description", descriptionBuff.toString());
	}
}
