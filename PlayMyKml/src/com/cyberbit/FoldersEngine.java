package com.cyberbit;

import modal.Config;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.regex.*;

import org.apache.commons.io.FilenameUtils;


public class FoldersEngine {
	
	Config config = new Config();
	
	public boolean run()
	{
		runOnConfigFolders();
		return true;
	}
	
	private boolean runOnConfigFolders(){
		String start_location = GetStartLocation();
		File file = new File(start_location);
		//Pattern pattern = Pattern.compile(config.GetFullPathLevel(1,0));
		//Matcher m = pattern.matcher(file.getPath());
		//move on start location and find folders that match to level_1
		
		ArrayList<String> arr = GetSubFolderMatchLavel(start_location, 0);
		
		return false;
	}
	
	private String GetStartLocation() {
		return "D:\\LFA";
		
	}
	
	private ArrayList<String> GetSubFolderMatchLavel(String base_path, int level)
	{
		if(config.GetFullPathLevel(level,0) == null){
			System.out.println("\n\ndirectory:" + base_path);
			MargeKmlFiles(base_path);
			return null;
			}
		String level_regex = config.GetFullPathLevel(level, 0);
		ArrayList<String> path_folders = new ArrayList<String>();
		ArrayList<String> all_subFolders = GetAllSubFolders(base_path);
		Pattern pattern = Pattern.compile(level_regex);
		Matcher m = pattern.matcher(base_path);
		if(m.find()){
			path_folders.add(base_path);
		}
		
		for (String subfolder : all_subFolders) {
			m = pattern.matcher(subfolder);
			if(m.find())
			{
				path_folders.add(subfolder);
				//path_folders.addAll(GetSubFolderMatchLavel(subfolder, level+1));
				GetSubFolderMatchLavel(subfolder, level+1);
			}
		}
		return path_folders;
	}

	private ArrayList<String> GetAllSubFolders(String base_path)
	{
		ArrayList<String> path_folders = new ArrayList<String>();
		File currentDir = new File(base_path);
		Matcher m;
		
		try {
			File[] files = currentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					String path = file.getPath().replaceAll("(?:!!)\\\\", "\\");
					path_folders.add(file.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return path_folders;
	}
	
	private void MargeKmlFiles(String base_path) {
		ArrayList<String> all_kml_files = GetAllKmlFiles(base_path);
		for (String string : all_kml_files) {
			System.out.println("kml file:" + string);
		}
		//KMLManager manager = new KMLManager(all_kml_files);
		//manager.Merge();
	}
	
	private ArrayList<String> GetAllKmlFiles(String base_path)
	{
		File currentDir = new File(base_path);
		ArrayList<String> path_kml_files = new ArrayList<String>();
		try {
			File[] files = currentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					path_kml_files.addAll(GetAllKmlFiles(file.getPath()));
				}
				else{
					String extension = FilenameUtils.getExtension(file.getPath());
					if(extension.equals("kml"))
						path_kml_files.add(file.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path_kml_files;
	}
}


