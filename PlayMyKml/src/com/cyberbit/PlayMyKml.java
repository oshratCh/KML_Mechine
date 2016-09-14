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
	public static void main(String[] args) {
	    
		PlayMyKml play = new PlayMyKml();
		String config_json_path = null;
		if(args.length != 0)
			config_json_path = args[0];
		play.run(config_json_path);
	}
	
	private void run(String config_json_path){
	
		KMLEngine kmlEngine = new KMLEngine();
		String started_location = GetStartLocation();
		kmlEngine.run(started_location);
	}
	
	private String GetConfigFilePath(String path){
		String default_config_path = new File("config.json").getAbsolutePath();
		
		if(path == null || path =="") 
				path = default_config_path;
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
			return path;
		}
		return default_config_path;
	}
	
	private String GetStartLocation() {
		//return "\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION";
		return "D:\\LFA";
		
	}
}