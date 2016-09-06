package modal;

import java.io.File;
import java.util.ArrayList;




public class Config {
	static Config config;
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
			level_1.add("KML:SERVICE_NAME");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\([0-9]*.[0-9]*)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_1|cyrcle_1|morning)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_3|cyrcle_3)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(reference)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(iphone)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(nexus|Android)");
			level_4.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(reference)");
			if(isUseKmlBody())
				isInKMLAction = true;
			isInKMLAction = false;
		}
		return config;
		
	}
	private static boolean isUseKmlBody() {
		if(CheckLevelIsUseKMLBody(level_1))
			return true;
		if(CheckLevelIsUseKMLBody(level_2))
			return true;
		if(CheckLevelIsUseKMLBody(level_3))
			return true;
		if(CheckLevelIsUseKMLBody(level_4))
			return true;
		if(CheckLevelIsUseKMLBody(level_5))
			return true;
		if(CheckLevelIsUseKMLBody(level_6))
			return true;
		if(CheckLevelIsUseKMLBody(level_7))
			return true;
		if(CheckLevelIsUseKMLBody(level_8))
			return true;
		return false;
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
	/*level_1.add("D:\\LFA\\([0-9].[0-9]*)");
			level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_1|cyrcle_1)");
			level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_2|cyrcle_2)");
			level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_3|cyrcle_3)");
			level_2.add("D:\\LFA\\[0-9].[0-9]*\\(reference)");
			level_3.add("D:\\LFA\\[0-9].[0-9]*\\(iphone)");
			level_3.add("D:\\LFA\\[0-9].[0-9]*\\(nexus)");
			level_3.add("D:\\LFA\\[0-9].[0-9]*\\(reference)");
			
			level_1.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\([0-9]*.[0-9]*)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_1|cyrcle_1|morning)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_2|cyrcle_2|afternoon)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\.*\\(cycle_3|cyrcle_3)");
			level_2.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(reference)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(iphone)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(nexus|Android)");
			level_3.add("\\\\192.168.10.95\\Home\\pcaps\\Oshrat\\LFA\\LOCATION\\[0-9]*.[0-9]*\\(reference)");*/
	
	public String GetFullPathLevel(int level, int num)
	{
		switch(level){
			case 0:
				if (level_1.size() > num)
					return level_1.get(num).replace("\\", "\\\\");
				break;
			case 1: if (level_2.size() > num)
						return level_2.get(num).replace("\\", "\\\\");
					break;
			case 2: if (level_3.size() > num)
						return level_3.get(num).replace("\\", "\\\\");
					break;
		}
		return null;
	}
	

}
