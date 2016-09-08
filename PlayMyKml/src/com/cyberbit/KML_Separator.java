package com.cyberbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modal.KML_Item;

public class KML_Separator {
	
	public static Set<String> GetAllValuesByKMLKey(String key, ArrayList<KML_Item> kmlList){
		Set<String> values = null ;
		for (KML_Item kmlItem : kmlList) {
			Map<String, List<Map<String, String>>> kmlPoints = kmlItem.getKMLPoints();
			Set<String> alGroups = kmlPoints.keySet();
			values.addAll(alGroups);
		}
		return values;
	}
	

}
