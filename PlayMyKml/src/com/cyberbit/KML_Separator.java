package com.cyberbit;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		values.remove("reference");
		return values;
	}
	
	
	public static Map<String, List<Map<String, String>>> GetAllPointsByKMLKey(String key, KML_Item item){
		String filter_key = key;
		filter_key = filter_key.trim();
		Map<String, List<Map<String, String>>> kml_points = item.getKMLPoints();
		if(kml_points.containsKey(filter_key)){
			List<Map<String, String>> points = kml_points.get(filter_key);
			kml_points.clear();
			kml_points.put(filter_key, points);
			return kml_points;
		}
		else{
			return null;
		}
	}	
	
	public static Boolean IsLogFile(KML_Item item){
		if(item.kml_path.contains("wbeout.log"))
			return true;
		return false;
		
	}
	
	public static ArrayList<KML_Item> CopyKMLItemPointsByServiceName( ArrayList<KML_Item> same_service_items, String service_key) {
		ArrayList<KML_Item> copy_list = new ArrayList<KML_Item>();
		for (KML_Item kml_Item : same_service_items) {
			KML_Item copy_Item = new KML_Item(kml_Item);
			copy_Item.setKMLPoints(KML_Separator.GetAllPointsByKMLKey(service_key, copy_Item));
			copy_list.add(copy_Item);
		}
		return copy_list;
	}
	
	public static Map<String, String> GetReferencePoint(KML_Item item ,ArrayList<KML_Item> references){
		ArrayList<Map<String, String>> reference_point = new ArrayList<>();
		Map<String, String> reference_first_point = null;
		if(item.GetLevel(1).contains("Stationary")){
			ArrayList<Map<String,String>> reference_first_points = GetReferenceFirstPoints(references);
			try {
				Entry<String, List<Map<String, String>>> entry = item.getKMLPoints().entrySet().iterator().next();
				reference_first_point = GetReferencePointToStationary(entry.getValue(), reference_first_points);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reference_first_point;
		
	}


	private static Map<String, String> GetReferencePointToStationary(
			Set<String> keySet,
			ArrayList<Map<String, String>> reference_first_points) throws ParseException{
		// TODO Auto-generated method stub
		return null;
	}


	private static Map<String, String> GetReferencePointToStationary( List<Map<String, String>> list, ArrayList<Map<String, String>> reference_first_point) throws ParseException {
		SimpleDateFormat ref_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat app_dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		ref_dateFormat.setLenient(false);
		app_dateFormat.setLenient(false);
		Date app_date = new Date(Long.parseLong(list.get(0).get("INTERCEPTION_INFO_INTERCEPTION_TIME")));

		for (int i = reference_first_point.size()-1; i >= 0; i--) {
			Map<String, String> reference_point = reference_first_point.get(i);
			String reference_date = reference_point.get("INTERCEPTION_INFO_INTERCEPTION_TIME");
			Date ref_date = ref_dateFormat.parse(reference_date);
			if(ref_date.compareTo(app_date) == -1)
				return reference_first_point.get(i);
		}
		return null;
	}


	private static ArrayList<Map<String, String>> GetReferenceFirstPoints(ArrayList<KML_Item> references) {
		ArrayList<Map<String, String>> points = new ArrayList<>();
		for (KML_Item kml_Item : references) {
			List<Map<String, String>> all_point = kml_Item.getKMLPoints().get("reference");
			points.add(all_point.get(0));
		}
		return points;
	}

}
