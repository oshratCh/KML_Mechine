package modal;

import java.io.File;
import java.util.ArrayList;




public class Config {
	ArrayList<String> level_1 = new ArrayList<String>();
	ArrayList<String> level_2 = new ArrayList<String>();
	ArrayList<String> level_3 = new ArrayList<String>();
	ArrayList<String> level_4 = new ArrayList<String>();
	ArrayList<String> level_5 = new ArrayList<String>();
	ArrayList<String> level_6 = new ArrayList<String>();
	ArrayList<String> level_8 = new ArrayList<String>();
	ArrayList<String> level_9 = new ArrayList<String>();
	
	
	public Config(){
		//Dates
		level_1.add("D:\\LFA\\([0-9].[0-9]*)");
		level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_1|cyrcle_1)");
		level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_2|cyrcle_2)");
		level_2.add("D:\\LFA\\[0-9].[0-9]*\\.*\\(cycle_3|cyrcle_3)");
		level_2.add("D:\\LFA\\[0-9].[0-9]*\\(reference)");
		level_3.add("D:\\LFA\\[0-9].[0-9]*\\(iphone)");
		level_3.add("D:\\LFA\\[0-9].[0-9]*\\(nexus)");
		level_3.add("D:\\LFA\\[0-9].[0-9]*\\(reference)");
		/*level_2.add("D:\\LFA\\.*\\Results.*\\.*(Cycle_1).*");
		level_2.add("D:\\LFA\\.*\\Results.*\\.*(Cycle_2).*");
		level_2.add("D:\\LFA\\.*\\Results.*\\.*(Cycle_3).*");
		level_3.add("D:\\LFA\\.*\\Results.*\\.*(iPhone).*");
		level_3.add("D:\\LFA\\.*\\Results.*\\.*(Nexus5).*");*/
		
	}
	
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
