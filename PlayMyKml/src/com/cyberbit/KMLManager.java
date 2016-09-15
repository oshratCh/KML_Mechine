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

import modal.Config;
import modal.KML_Item;

import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LabelStyle;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;


public class KMLManager {
	
	Kml my_kml;
	Document kml_main_doc;
	Log_Converter log_Converter = new Log_Converter();
	ArrayList<KML_Item> KML_list;
	
	String by_class_filte_level;
	int by_class_filte_num;
	String by_service_filter_serviceKey;
	Predicate<KML_Item> by_class_filter = new Predicate<KML_Item>() {
		
		@Override
		public boolean test(KML_Item arg0) {
			if(arg0 == null || (!arg0.GetLevel(by_class_filte_num).equalsIgnoreCase(by_class_filte_level)))
				return false;
			return true;
		}
	};
	
	Predicate<KML_Item> by_service_filter = new Predicate<KML_Item>() {
		
		@Override
		public boolean test(KML_Item arg0) {
			if(arg0 == null || !arg0.getKMLPoints().containsKey(by_service_filter_serviceKey) && !arg0.kml_path.contains("reference"))
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
		LabelStyle style_label =  style.createAndSetLabelStyle();
		style_label.withScale(0.0d);
		
		de.micromata.opengis.kml.v_2_2_0.Style reference_style = kml_main_doc.createAndAddStyle().withId("reference");
		LabelStyle reference_label =  reference_style.createAndSetLabelStyle();
		reference_label.withColor("ff0000ff");
		reference_label.setColorMode(ColorMode.NORMAL);
		reference_label.withScale(0.5d);
		IconStyle reference_icon = reference_style.createAndSetIconStyle();
		reference_icon.setScale(1.0d);
		reference_icon.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png");
		
		Folder root_folder = kml_main_doc.createAndAddFolder();
		root_folder.setName("Root KML");
		if(Config.isContainsKMLAction())
			AddAllKMLSwithInKMLAction(KML_list,0,root_folder);
		else
			AddAllKMLS(KML_list,0, root_folder);
		
		try {
			my_kml.marshal(new File("./tmp/mergedFile.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
	public Boolean runAddWinFolders(String merged_kml_folder ,ArrayList<KML_Item> KML_list){
		String inKml_level = KML_list.get(0).GetLevel(0);
		inKml_level = inKml_level.split("KML:")[1].trim();
		Set<String> all_values = KML_Separator.GetAllValuesByKMLKey(inKml_level, KML_list);
		for (String key_value : all_values) {
			//Folder folder = AddNewFolder(key_value, kml);
			by_service_filter_serviceKey = key_value;
			ArrayList<KML_Item> same_service_items =  (ArrayList<KML_Item>)(Object)KML_list.stream().filter(by_service_filter).collect(Collectors.toList());
			same_service_items = filterKMLItemPointsByServiceName(same_service_items, key_value);
			//AddAllKMLS(same_service_items, 0,folder);
		}
		return true;
		
	}
	
	private void CreateKML(String merged_kml_folder ,ArrayList<KML_Item> KML_list){
		my_kml = KmlFactory.createKml();
		kml_main_doc=my_kml.createAndSetDocument();
		kml_main_doc.setName("Marge kml");
		de.micromata.opengis.kml.v_2_2_0.Style style = kml_main_doc.createAndAddStyle();
		style.setId("style");
		IconStyle iconStyle=style.createAndSetIconStyle();
		iconStyle.setScale(1.0d);
		Icon grn_pushpin = iconStyle.createAndSetIcon();
		grn_pushpin.setHref("http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png");
		LabelStyle style_label =  style.createAndSetLabelStyle();
		style_label.withScale(0.0d);
		
		de.micromata.opengis.kml.v_2_2_0.Style reference_style = kml_main_doc.createAndAddStyle().withId("reference");
		LabelStyle reference_label =  reference_style.createAndSetLabelStyle();
		reference_label.withColor("ff0000ff");
		reference_label.setColorMode(ColorMode.NORMAL);
		reference_label.withScale(0.5d);
		IconStyle reference_icon = reference_style.createAndSetIconStyle();
		reference_icon.setScale(1.0d);
		reference_icon.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png");
		
		Folder root_folder = kml_main_doc.createAndAddFolder();
		root_folder.setName("Root KML");
		
		AddAllKMLSwithInKMLAction(KML_list,0,root_folder);
		
		try {
			my_kml.marshal(new File("./tmp/"+merged_kml_folder+"/mergedFile.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean AddAllKMLS(ArrayList<KML_Item> items, int num_level, Folder parent){
		String kmls_levels = "";
		
		for (int i = 0; i < items.size(); i++) {
			int max_num_level = items.get(i).GetNumOfLevels();
			if( items.size()!= 1  && num_level !=  max_num_level && (num_level >  max_num_level || !items.get(i).GetLevel(num_level).equals(kmls_levels) ))
			{
				kmls_levels = items.get(i).GetLevel(num_level);
				by_class_filte_level = items.get(i).GetLevel(num_level);
				by_class_filte_num = num_level;
				ArrayList<KML_Item> same_level_items =  (ArrayList<KML_Item>)(Object)items.stream().filter(by_class_filter).collect(Collectors.toList());
				Folder folder = AddNewFolder(kmls_levels, parent);
				AddAllKMLS(same_level_items, num_level+1, folder);
			}
			else{
				if(items.size() == 1 || num_level == max_num_level){
					Folder folder = parent;
					if( num_level != max_num_level){
						folder = AddNewFolder(items.get(i).GetLevel(num_level), parent);
					}
					AddNewKML(items.get(i), folder);
				}
			}
		}
		return true;
	}
	
	private Boolean AddAllKMLSwithInKMLAction(ArrayList<KML_Item> items, int num_level, Folder parent){
		String inKml_level = items.get(0).GetLevel(num_level);
		inKml_level = inKml_level.split("KML:")[1].trim();
		Set<String> all_values = KML_Separator.GetAllValuesByKMLKey(inKml_level, items);
		for (String key_value : all_values) {
			Folder folder = AddNewFolder(key_value, parent);
			by_service_filter_serviceKey = key_value;
			ArrayList<KML_Item> same_service_items =  (ArrayList<KML_Item>)(Object)items.stream().filter(by_service_filter).collect(Collectors.toList());
			same_service_items = filterKMLItemPointsByServiceName(same_service_items, key_value);
			AddAllKMLS(same_service_items, num_level+1,folder);
		}
		return true;
	}
	
	
	
	
	
	private ArrayList<KML_Item> filterKMLItemPointsByServiceName( ArrayList<KML_Item> same_service_items, String service_key) {
		ArrayList<KML_Item> copy_list = new ArrayList<KML_Item>(same_service_items);
		for (KML_Item kml_Item : copy_list) {
			KML_Item copy_item = new KML_Item(kml_Item);
			copy_item.setKMLPoints(KML_Separator.GetAllPointsByKMLKey(service_key, copy_item));
		}
		return same_service_items;
	}

	private Folder AddNewFolder(String FolderName, Folder parent){
			Folder folder = parent.createAndAddFolder();
			folder.setName(FolderName);
		return folder;
		
	}
		
	private Boolean AddNewKML(KML_Item item, Folder parent){
		if(Config.isContainsKMLAction()){
			AddNewKMLInKMLAction(item, parent);
		}
		else{
			AddNewKMLNoInKMLAction(item, parent);
		}
		return true;
		
	}
	
	private Boolean AddNewKMLNoInKMLAction(KML_Item item, Folder parent){
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
				AddNewGroup(allPlacemarks1, "reference" ,folder);	

				
			}
		}
		return true;
	}
	
	private Boolean AddNewKMLInKMLAction(KML_Item item, Folder parent){
		//Folder folder = AddNewFolder(kml_name, parent);
		if(item.kml_path.endsWith(".log"))
			AddNewGroup(item.getKMLPoints().get(by_service_filter_serviceKey), "" ,parent);
		if(item.kml_path.endsWith(".kml")){
			String kml_name =  FilenameUtils.getBaseName(item.kml_path);
			Folder folder = AddNewFolder(kml_name, parent);
			AddNewGroup(item.getKMLPoints().get("reference"), "reference" ,folder);
		}
		return true;
	}
	
	
	
	private Boolean AddNewGroup(List<Map<String, String>> allPlacemarks, String groupValue ,Folder folder){
		int index = 1;
		for (Map<String, String> map : allPlacemarks) {
			Placemark placemark = folder.createAndAddPlacemark();
			//Remove name option
			if(groupValue.equals("reference")){
				placemark.setName("P. "+index);
				placemark.setStyleUrl("#reference");
			}
			else{
				placemark.setName(groupValue +" p. "+index);
				placemark.setStyleUrl("#style");
			}
			placemark.setDescription(map.get("description"));
			Point point=placemark.createAndSetPoint();
			double longitude=Double.parseDouble(map.get("MAIL_FROM_LOCATION_LONGITUDE"));
			double latitude=Double.parseDouble(map.get("MAIL_FROM_LOCATION_LATITUDE"));
			point.addToCoordinates(longitude, latitude);
			index++;
		}
		return true;
		
	}

	
	

}
