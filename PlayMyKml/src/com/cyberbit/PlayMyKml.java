package com.cyberbit;

import modal.Config;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class PlayMyKml {	
	/**
	 * @param args
	 */	
	static String default_config_path;
	public static void main(String[] args) {
	    default_config_path = new File("config.json").getAbsolutePath();
		System.out.println("default_config_path:" + default_config_path);
		PlayMyKml play = new PlayMyKml();
		String config_json_path = null;
		if(args.length != 0)
			config_json_path = args[0];
		play.run(config_json_path);
		/*Config config = null;*/
	    /*ObjectMapper mapper = new ObjectMapper();*/
	}
	
	private void run(String config_json_path){
		/*ObjectMapper mapper = new ObjectMapper();
		Config configs = null;
		
		try{
			// Convert JSON string from file to Object
			File file = new File(GetConfigFilePath(config_json_path));
			configs = mapper.readValue(file, Config.class);
			
			System.out.println(configs);
			
		}catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*FoldersEngine foldersEngine = new FoldersEngine();
		foldersEngine.run();*/
		
		KMLEngine kmlEngine = new KMLEngine();
		String started_location = GetStartLocation();
		kmlEngine.run(started_location);
	}
	
	private String GetConfigFilePath(String path){
		if(path == null || path =="") return default_config_path;
		File f = new File(GetConfigFilePath(path));
		if(f.exists() && !f.isDirectory()) { 
			return path;
		}
		return default_config_path;
	}
	
	private String GetStartLocation() {
		return "D:\\LFA";
		
	}
}