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
		play.run();
	}
	
	private void run(){
	
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
		Config config = Config.GetIntance();
		return Config.startLocation;
		
	}
}