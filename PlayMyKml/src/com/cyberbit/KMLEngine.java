package com.cyberbit;

import java.io.File;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import modal.Config;
import modal.KML_Item;

public class KMLEngine {
	Config config = Config.GetIntance();
	KMLManager kmlManager ;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
	
	public boolean run(String started_location)
	{
		dateFormat.setLenient(false);
		
		String start_location = started_location;
		ArrayList<Path> all_kmls = GetAllKmlFiles(start_location);
		ArrayList<KML_Item> KML_list = new ArrayList<>();
		
		for (Path path : all_kmls) {
			KML_Item kml_item = GetAllKMLByMatchLevels(path.toString());
			if(kml_item != null)
				KML_list.add(kml_item);
		}
		
		//Sort the KML_list list by levels
		Collections.sort(KML_list, new Comparator() {

	        public int compare(Object o1, Object o2) {

	            int level_num = 0, sComp = 0;
	            String level_folder = "";
	            while(level_folder != null && !level_folder.equals("NONE") && sComp == 0){
	            	String x1 = ((KML_Item) o1).GetLevel(level_num);
		            String x2 = ((KML_Item) o2).GetLevel(level_num);
		            if(isValidDate(x1) && isValidDate(x2))
		            {
		            	Date x1date = null;
		            	Date x2date = null;
		            	try {
							x1date = dateFormat.parse(x1.trim());
							x2date = dateFormat.parse(x2.trim());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	sComp = x1date.compareTo(x2date);
		            }
		            else{
		            	sComp = x1.compareToIgnoreCase(x2);
		            }
		            level_num++;
		            level_folder = ((KML_Item) o1).GetLevel(level_num);
	            }
	            return sComp;
	        }
	            	
	    });
		
		for (KML_Item folders : KML_list) {
			System.out.println("path:" + folders.kml_path);
			System.out.println("-----folders:" + folders.GetLevel(0));
			System.out.println("-----folders:" + folders.GetLevel(1));
			System.out.println("-----folders:" + folders.GetLevel(2));
			System.out.println("-----folders:" + folders.GetLevel(3));
		}
		
		kmlManager = new KMLManager();
		kmlManager.run(KML_list);
		
		return true;
	}
	
	protected boolean isValidDate(String inDate) {
	    try {
	      dateFormat.parse(inDate.trim());
	    } catch (ParseException pe) {
	      return false;
	    }
	    return true;
	  }
	

	
	
	private ArrayList<Path> GetAllKmlFiles(String base_path)
	{
		File currentDir = new File(base_path);
		ArrayList<Path> path_kml_files = new ArrayList<Path>();
		try {
			File[] files = currentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					path_kml_files.addAll(GetAllKmlFiles(file.getPath()));
				}
				else{
					String extension = FilenameUtils.getExtension(file.getPath());
					String file_name = FilenameUtils.getBaseName(file.getPath());
					if( file_name.equals("wbeout") && extension.equals("log") || file_name.startsWith("Track") && extension.equals("kml") )
						path_kml_files.add(file.toPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path_kml_files;
	}
	
	public KML_Item GetAllKMLByMatchLevels(String kml_path)
	{
		ArrayList<Map<String, String>> matches = CheckKMLAcorrdindConfigLevels(kml_path);
		System.out.println("******************************************************************");
		System.out.println("kml path:" + kml_path);
		for (Map<String,String> match : matches) {
			System.out.println("-----match path:" + match.toString());
		}
		//TODO: change number 3 to count of levels
		if(matches.size() == 3)
		{
			ArrayList<String> foldersArrayList =  getArrayOfFoldersName(matches);
			return new KML_Item(kml_path, foldersArrayList);	
		}
		return null;
		
	}
		

	private ArrayList<Map<String, String>> CheckKMLAcorrdindConfigLevels(String kml_path) {
		
		String level_regex = config.GetFullPathLevel(0,0);
		int level=0,  level_inner= 0;
		ArrayList<Map<String, String>> path_arrayList = new ArrayList<Map<String, String>>();
		// each level
		while (level_regex != null) {
			// inner array
			//this flag is to help us to get only the first level that the kml match, so we can filter the kml that path_arrayList items account match to levels account.
			Boolean match_level = false;
			while(level_regex != null && !match_level){
				Pattern pattern = Pattern.compile(level_regex+".*", Pattern.CASE_INSENSITIVE);
				Matcher m = pattern.matcher(kml_path);
				if(m.find()){
					Map<String, String> map = new HashMap<String, String>();
					map.put("kml_path", kml_path);
					map.put("level_regex", level_regex);
					path_arrayList.add(map);
					match_level = true;
				}
				level_inner ++;
				level_regex = config.GetFullPathLevel(level, level_inner);
			}
			level_inner = 0;
			level++;
			level_regex = config.GetFullPathLevel(level, level_inner);			
		}
		return path_arrayList;
	}

	private ArrayList<String> getArrayOfFoldersName(ArrayList<Map<String, String>> matches) {
		ArrayList<String> list = new ArrayList<String>();
		
		for (Map<String, String> level_match : matches) {
			String match_string = level_match.get("level_regex");
			Pattern pattern = Pattern.compile(match_string+".*", Pattern.CASE_INSENSITIVE);
			Matcher m = pattern.matcher(level_match.get("kml_path"));
			if(m.find()){
				String result = m.group(1);
				if(result.startsWith("cyrcle"))
					result = result.replace("cyrcle", "cycle");
				if(result.startsWith("Nexus"))
					result = result.replace("Nexus", "nexus");
				if(result.startsWith("iPhone"))
					result = result.replace("iPhone", "iphone");
				if(result.startsWith("morning"))
					result = result.replace("morning", "cycle_1");
				if(result.startsWith("afternoon"))
					result = result.replace("afternoon", "cycle_2");
				list.add(result);
			}
		}
		return list;
	}
}
