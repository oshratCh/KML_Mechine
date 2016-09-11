package com.cyberbit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modal.KML_Item;

public class KML_Separator {
	
	/*to Get all the apps name */
	public static Set<String> GetAllValuesByKMLKey(String key, ArrayList<KML_Item> kmlList){
		Set<String> values = new HashSet<String>();
		for (KML_Item kmlItem : kmlList) {
			Map<String, List<Map<String, String>>> kmlPoints = kmlItem.getKMLPoints();
			Set<String> alGroups = kmlPoints.keySet();
			values.addAll(alGroups);
		}
		return values;
	}
	
	
	public static List<Map<String, String>> GetAllPointsByKMLKey(String key, KML_Item item){
		String filter_key = key;
		filter_key = filter_key.trim();
		Map<String, List<Map<String, String>>> kml_points = item.getKMLPoints();
		if(kml_points.containsKey(filter_key)){
			return kml_points.get(filter_key);
		}
		else{
			return null;
		}
	}	

}
