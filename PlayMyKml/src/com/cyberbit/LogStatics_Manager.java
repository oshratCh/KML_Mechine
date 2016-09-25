package com.cyberbit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.micromata.opengis.kml.v_2_2_0.Folder;

import modal.Config;
import modal.KML_Item;


public class LogStatics_Manager {
	ArrayList<KML_Item> KML_list= null;
	File log_file;
	BufferedWriter bufferedWriter;
	ArrayList<Double> app_distances = new ArrayList<>();
	ArrayList<Double> daily_distances = new ArrayList<>();
	
	String by_class_filte_level;
	int by_class_filte_num;
	String by_service_filter_serviceKey;
	Predicate<KML_Item> by_class_filter = new Predicate<KML_Item>() {
		
		@Override
		public boolean test(KML_Item arg0) {
			if(arg0 == null || (!arg0.GetLevel(by_class_filte_num).equalsIgnoreCase(by_class_filte_level)) || arg0.kml_path.contains("reference"))
				return false;
			return true;
		}
	};
	
	Predicate<KML_Item> get_references_filter = new Predicate<KML_Item>() {
		
		@Override
		public boolean test(KML_Item arg0) {
			if(arg0 == null || !arg0.kml_path.contains("reference"))
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
		log_file = new File("./tmp/static.log");
		try {
			log_file.createNewFile();
			FileWriter fileWriter = new FileWriter(log_file);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			FillLogFile(KML_list);
			
			bufferedWriter.flush();
			bufferedWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
	public Boolean FillLogFile(ArrayList<KML_Item> KML_list){
		String inKml_level = KML_list.get(0).GetLevel(0);
		inKml_level = inKml_level.split("KML:")[1].trim();
		Set<String> all_values = KML_Separator.GetAllValuesByKMLKey(inKml_level, KML_list);
		for (String key_value : all_values) {
			//Create win folder
			by_service_filter_serviceKey = key_value;
			ArrayList<KML_Item> same_service_items =  (ArrayList<KML_Item>)(Object)KML_list.stream().filter(by_service_filter).collect(Collectors.toList());
			same_service_items = KML_Separator.CopyKMLItemPointsByServiceName(same_service_items, key_value);
			ArrayList<KML_Item> references = (ArrayList<KML_Item>)(Object)KML_list.stream().filter(get_references_filter).collect(Collectors.toList());
			if(same_service_items.size() > 0 ){
				AddHeader(key_value, 0);
				AddHeader("================================", 0);
				AddItemsToFile(same_service_items, 1, references);
			}
		}
		return true;
		
	}
	
	private void AddItemsToFile(ArrayList<KML_Item> items, int num_level, ArrayList<KML_Item> references ){
		String kmls_levels = "";
		
		for (int i = 0; i < items.size(); i++) {
			int max_num_level = items.get(i).GetNumOfLevels();
			if( items.size()!= 1  && num_level !=  max_num_level && (num_level >  max_num_level || !items.get(i).GetLevel(num_level).equals(kmls_levels) ))
			{
				kmls_levels = items.get(i).GetLevel(num_level);
				by_class_filte_level = items.get(i).GetLevel(num_level);
				by_class_filte_num = num_level;
				ArrayList<KML_Item> references_items = new ArrayList<>();
				ArrayList<KML_Item> same_level_items =  (ArrayList<KML_Item>)(Object)items.stream().filter(by_class_filter).collect(Collectors.toList());
				if(same_level_items.size() > 0){
					AddHeader(kmls_levels, num_level);
					if(same_level_items.size() == 1 || num_level+1 == max_num_level){
						references_items = (ArrayList<KML_Item>)(Object)references.stream().filter(get_references_filter).collect(Collectors.toList());
						AddItemsToFile(same_level_items, num_level+1, references_items);	
					}
					else{
						AddItemsToFile(same_level_items, num_level+1, references);
					}
				}
			}
			else{
				if((items.size() == 1 || num_level == max_num_level) && (items.get(i).GetLevel(num_level -1) == null || !items.get(i).GetLevel(num_level-1).equalsIgnoreCase("reference"))){
					if( num_level != max_num_level){
						AddHeader(items.get(i).GetLevel(num_level), num_level);
					}
					 List<Double> a = AddNewItem(items.get(i), references, num_level);
					 app_distances.addAll(a);
					 daily_distances.addAll(a);
				}
			}
		}
		if(num_level == 2 && items.size() > 0){
			Double avg = calculateAverage(daily_distances);
			AddHeader("------------------------------------------------------------", 2);
			AddHeader("++ Daily avg: "+ avg +"   sd: " +getStandardDeviation(daily_distances, avg), 2);
			daily_distances = new ArrayList<>();
		}
		if(num_level == 1){
			AddHeader("-----------------------------------------------------------------------------------------", 1);
			Double avg = calculateAverage(app_distances);
			AddHeader("++ App avg: " + avg +"    sd: " +getStandardDeviation(app_distances, avg) , 1);
			app_distances = new ArrayList<>();
		}
	}
	
	private Double getStandardDeviation(ArrayList<Double> numbers, Double average){
		Double sd = 0d;
		for (int i=0; i<numbers.size();i++){
			sd = sd + Math.pow(numbers.get(i) - average, 2);
        }
		sd = Math.sqrt(sd/(numbers.size()));
		return sd;

	}
	
	private Folder AddHeader(String kmls_level, int num_level) {
		String header = "";
		header = header + StringUtils.repeat("\t", num_level) + kmls_level+ "\n";
		try {
			bufferedWriter.write(header);
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private List<Double> AddNewItem(KML_Item kml_Item, ArrayList<KML_Item> references, int num_level) {
		List<Double> distances = GetPointsStatic(kml_Item, references);
		Double max = Collections.max(distances);
		Double min = Collections.min(distances);
		Double avg = calculateAverage(distances);
		String header = StringUtils.repeat("\t", num_level)+"avg: " + avg  +"   best: "+ min + "   worst: "+ max  +"\n";
		try {
			bufferedWriter.write(header);
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distances;
	}

	private List<Double> GetPointsStatic(KML_Item kml_Item, ArrayList<KML_Item> references) {
		Map<String, String> reference_point = KML_Separator.GetReferencePoint(kml_Item, references);
		Double ref_lon = Double.parseDouble(reference_point.get("MAIL_FROM_LOCATION_LONGITUDE"));
		Double ref_lat = Double.parseDouble(reference_point.get("MAIL_FROM_LOCATION_LATITUDE"));
		List<Double> distance = new ArrayList<>();
		List<Map<String, String>> kml_points = kml_Item.getKMLPoints().entrySet().iterator().next().getValue();
		Boolean f = true;
		for (Map<String, String> kml_point : kml_points) {
			Double app_lon = Double.parseDouble(kml_point.get("MAIL_FROM_LOCATION_LONGITUDE"));
			Double app_lat = Double.parseDouble(kml_point.get("MAIL_FROM_LOCATION_LATITUDE"));
			double d = calculateDistance(app_lat, app_lon,ref_lat, ref_lon)*1000;
			if(f)
				System.out.println("path "+ kml_Item.kml_path+"\n\t"+ "coords: "+ app_lon + ","+ app_lat + "\n\t"+ "reference: "+ ref_lon+ ","+ ref_lat+ "\n\t distance: "+ d);
			f=false;
			distance.add(d);
		}
		return distance;
	}
	
	private double calculateDistance(double lat1,double lon1,double lat2,double lon2) {
		double R = 6371; // Radius of the earth in km
		double dLat = deg2rad(lat2-lat1);  // deg2rad below
		double dLon = deg2rad(lon2-lon1); 
		double a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2)
		    ; 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c; // Distance in km
		  return d;
		}

		private double deg2rad(double deg) {
		  return deg * (Math.PI/180);
		}
	
	private double calculateAverage(List<Double> distance) {
		Double sum = 0d;
	  if(!distance.isEmpty()) {
	    for (Double mark : distance) {
	        sum += mark;
	    }
	    return sum.doubleValue() / distance.size();
	  }
	  return sum;
	}
	

}
