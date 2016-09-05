package com.cyberbit;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import modal.KML_Item;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;


public class KMLManager {
	
	Kml my_kml;
	Document kml_main_doc;
	Log_Converter log_Converter = new Log_Converter();
	ArrayList<KML_Item> KML_list;
	
	String predicate_level;
	int predicate_num;
	Predicate<KML_Item> predicate = new Predicate<KML_Item>() {
		
		@Override
		public boolean test(KML_Item arg0) {
			if(arg0 == null || !arg0.GetLevel(predicate_num).equalsIgnoreCase(predicate_level))
				return false;
			return true;
		}
	};
	
	public Boolean run(ArrayList<KML_Item> KML_list){
		this.KML_list = KML_list;
		my_kml = KmlFactory.createKml();
		kml_main_doc=my_kml.createAndSetDocument();
		kml_main_doc.setName("Marge kml");
		de.micromata.opengis.kml.v_2_2_0.Style style = kml_main_doc.createAndAddStyle();
		style.setId("style");
		IconStyle iconStyle=style.createAndSetIconStyle();
		iconStyle.setScale(1.0d);
		Icon grn_pushpin = iconStyle.createAndSetIcon();
		grn_pushpin.setHref("http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png");
		Folder root_folder = kml_main_doc.createAndAddFolder();
		root_folder.setName("Root KML");
		AddAllKMLS(KML_list,0, root_folder);
		
		try {
			my_kml.marshal(new File("./tmp/mergedFile.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
	private Boolean AddAllKMLS(ArrayList<KML_Item> items, int num_level, Folder parent){
		String kmls_levels = "";
		
		for (int i = 0; i < items.size(); i++) {
			int max_num_level = items.get(i).GetNumOfLevels();
			if( items.size()!= 1  && num_level !=  max_num_level && (num_level >  max_num_level || !items.get(i).GetLevel(num_level).equals(kmls_levels) ))
			{
				kmls_levels = items.get(i).GetLevel(num_level);
				predicate_level = items.get(i).GetLevel(num_level);
				predicate_num = num_level;
				ArrayList<KML_Item> same_level_items =  (ArrayList<KML_Item>)(Object)items.stream().filter(predicate).collect(Collectors.toList());
				Folder folder = AddNewFolder(kmls_levels, parent);
				AddAllKMLS(same_level_items, num_level+1, folder);
			}
			else{
				if(items.size() == 1 || num_level == max_num_level){
					AddNewKML(items.get(i), parent);
				}
			}
		}
		return true;
	}
	
	private Folder AddNewFolder(String FolderName, Folder parent){
			Folder folder = parent.createAndAddFolder();
			folder.setName(FolderName);
		return folder;
		
	}
		
	private Boolean AddNewKML(KML_Item item, Folder parent){
		if(item.kml_path.endsWith(".log")){
			Map<String, List<Map<String, String>>> allPlacemarks = log_Converter.GetPointsByServiceName(item.kml_path);
			Set<String> alGroups = allPlacemarks.keySet();
			for (String groupValue : alGroups) {
				Folder folder = AddNewFolder(groupValue, parent);
				List<Map<String, String>> allPlacemarks1 = allPlacemarks.get(groupValue);
				AddNewGroup(allPlacemarks1, groupValue ,folder);
			}
		}
		else{
			if(item.kml_path.endsWith(".kml")){
				KML_Converter kml_Converter = new KML_Converter();
				List<Map<String, String>> allPlacemarks1 = kml_Converter.parseFileData(item.kml_path);
				String kml_name =  FilenameUtils.getBaseName(item.kml_path);
				Folder folder = AddNewFolder(kml_name, parent);
				AddNewGroup(allPlacemarks1, "" ,folder);	

				
			}
		}
		return true;
	}
	
	
	
	private Boolean AddNewGroup(List<Map<String, String>> allPlacemarks, String groupValue ,Folder folder){
		int index = 1;
		for (Map<String, String> map : allPlacemarks) {
			Placemark placemark = folder.createAndAddPlacemark();
			placemark.setName(groupValue +" P. "+index);
			placemark.setDescription(map.get("description"));
			placemark.setStyleUrl("#style");
			Point point=placemark.createAndSetPoint();
			double longitude=Double.parseDouble(map.get("MAIL_FROM_LOCATION_LONGITUDE"));
			double latitude=Double.parseDouble(map.get("MAIL_FROM_LOCATION_LATITUDE"));
			point.addToCoordinates(longitude, latitude);
			index++;
		}
		return true;
		
	}

	
	

}
