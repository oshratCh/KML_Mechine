package modal;

import java.io.File;
import java.util.ArrayList;




public class Config {
	static Config config;
	public static String startLocation;
	static String mode;
	static ArrayList<String> level_1 = new ArrayList<String>();
	static ArrayList<String> level_2 = new ArrayList<String>();
	static ArrayList<String> level_3 = new ArrayList<String>();
	static ArrayList<String> level_4 = new ArrayList<String>();
	static ArrayList<String> level_5 = new ArrayList<String>();
	static ArrayList<String> level_6 = new ArrayList<String>();
	static ArrayList<String> level_7 = new ArrayList<String>();
	static ArrayList<String> level_8 = new ArrayList<String>();
	static Boolean isInKMLAction = false;
	
	
	public static Config GetIntance(){
		if(config == null){
			config = new Config();
			//base path
			startLocation = "\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION";
			mode = "statics"; 
			level_1.add("WIN KML:SERVICE_NAME");
			level_1.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\([0-9]*.[0-9]*[A-Za-z0-9_]*Stationary)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_1|cyrcle_1|morning)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_3|cyrcle_3)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(iphone)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(nexus|Android)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)");

			if(isUseKmlBody())
				isInKMLAction = true;
			else
				isInKMLAction = false;
		}
		return config;
		
	}
	
	public static Boolean IsStaticsMode(){
		if(mode.equalsIgnoreCase("statics"))
			return true;
		return false;
	}
	private static boolean isUseKmlBody() {
		if(GetAllInKMLLevels().size() > 0)
			return true;
		return false;
	}
	
	public static boolean isCreateWinFolders() {
		if(isUseKmlBody() && level_1.get(0).trim().startsWith("WIN"))
			return true;
		return false;
	}
	
	public static ArrayList<ArrayList<String>> GetAllInKMLLevels(){
		ArrayList<ArrayList<String>> levels = new ArrayList<>();
		if(CheckLevelIsUseKMLBody(level_1))
			levels.add(level_1);
		if(CheckLevelIsUseKMLBody(level_2))
			levels.add(level_2);
		if(CheckLevelIsUseKMLBody(level_3))
			levels.add(level_3);
		if(CheckLevelIsUseKMLBody(level_4))
			levels.add(level_4);
		if(CheckLevelIsUseKMLBody(level_5))
			levels.add(level_5);
		if(CheckLevelIsUseKMLBody(level_6))
			levels.add(level_6);
		if(CheckLevelIsUseKMLBody(level_7))
			levels.add(level_7);
		if(CheckLevelIsUseKMLBody(level_8))
			levels.add(level_8);
		return levels;
	}
	
	
	private static boolean CheckLevelIsUseKMLBody(ArrayList<String> level) {
		for (String level_string : level) {
			if(level_string.contains("KML:"))
				return true;
		}
		return false;
	}
	
	public static Boolean isContainsKMLAction(){
		if(isInKMLAction != null )
			return isInKMLAction;
		return false;
			
	}
	/*	
			//Task 1: merge all kmls to one big kml
			mode =  "create";
			level_1.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\([0-9]*.[0-9]*[A-Za-z0-9_]*)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\.*\\(cycle_1|cyrcle_1|morning)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\.*\\(cycle_3|cyrcle_3)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\(reference)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\(iphone)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\(nexus|Android)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*\\(reference)");
			
			//Task 2: create folders for each protocol,
			// in every folder one kml with the point from the protocol and references points
			// order by Date -> Cycle -> Device or Reference
			mode =  "create";
			level_1.add("KML:SERVICE_NAME");
			level_2.add("D:\\LFA\\([0-9]*.[0-9]*)");
			level_3.add("D:\\LFA\\[0-9]*.[0-9]*\\.*\\(cycle_1|cyrcle_1|morning)");
			level_3.add("D:\\LFA\\[0-9]*.[0-9]*\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_3.add("D:\\LFA\\[0-9]*.[0-9]*\\.*\\(cycle_3|cyrcle_3)");
			level_3.add("D:\\LFA\\[0-9]*.[0-9]*\\(reference)");
			level_4.add("D:\\LFA\\[0-9]*.[0-9]*\\(iphone)");
			level_4.add("D:\\LFA\\[0-9]*.[0-9]*\\(nexus|Android)");
			level_4.add("D:\\LFA\\[0-9]*.[0-9]*\\(reference)");
			
			//Task 3: create one statics log file, each protocol -> date -> cycle -> device 
			// avg distance from the reference point
			// daily avg and standard deviation 
			// each app sum avg and standard deviation
			
			mode = "statics"; 
			level_1.add("WIN KML:SERVICE_NAME");
			level_1.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\([0-9]*.[0-9]*[A-Za-z0-9_]*Stationary)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_1|cyrcle_1|morning)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\.*\\(cycle_3|cyrcle_3)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(iphone)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(nexus|Android)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*[A-Za-z0-9_]*Stationary\\(reference)"); */
	
	public String GetFullPathLevel(int level, int num)
	{
		switch(level){
			case 0:
				if (level_1.size() > num)
					return level_1.get(num).replace("\\", "\\\\");
				break;
			case 1: 
				if (level_2.size() > num)
					return level_2.get(num).replace("\\", "\\\\");
				break;
			case 2: 
				if (level_3.size() > num)
					return level_3.get(num).replace("\\", "\\\\");
				break;
			case 3: 
				if (level_4.size() > num)
					return level_4.get(num).replace("\\", "\\\\");
				break;
		}
		return null;
	}
	
	public int GetNumOfLevels(){
		int num = GetNumOfAllLevels();
		ArrayList<ArrayList<String>> in_kml_levels = GetAllInKMLLevels();
		return num - in_kml_levels.size();
	}
	
	public int GetNumOfAllLevels(){
		if(level_1 == null || level_1.isEmpty())
			return 0;
		if(level_2 == null || level_2.isEmpty())
			return 1;
		if(level_3 == null || level_3.isEmpty())
			return 2;
		if(level_4 == null || level_4.isEmpty())
			return 3;
		if(level_5 == null || level_5.isEmpty())
			return 4;

		return 5;
	}
	
	

}
