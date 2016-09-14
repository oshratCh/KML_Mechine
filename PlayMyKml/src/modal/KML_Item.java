package modal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KML_Item {
	public String kml_path;
	String level_1;
	String level_2;
	String level_3;
	String level_4;
	String level_5;
	String level_6;
	Map<String, List<Map<String, String>>> kml_points;
	
	public KML_Item(String kml_path,ArrayList<String> levels){
		this.kml_path = kml_path;
		if(levels.size() > 0)
			this.level_1  =  levels.get(0);
		if(levels.size() > 1)
			this.level_2  = levels.get(1);
		if(levels.size() > 2)
			this.level_3  = levels.get(2);
		if(levels.size() > 3)
			this.level_4  = levels.get(3);
		if(levels.size() > 4)
			this.level_5  = levels.get(4);
		if(levels.size() > 5)
			this.level_6  = levels.get(5);
	}
	public KML_Item(KML_Item item){
		this.kml_path = item.kml_path;
		this.level_1 = item.level_1;
		this.level_2 = item.level_2;
		this.level_3 = item.level_3;
		this.level_4 = item.level_4;
		this.level_5 = item.level_5;
		this.level_6 = item.level_6;
		
		this.kml_points = new HashMap<String, List<Map<String,String>>>(item.getKMLPoints());
	}
	
	public void setKMLPoints(Map<String, List<Map<String, String>>> points){
		kml_points = points;
	}
	
	public Map<String, List<Map<String, String>>> getKMLPoints(){
		return kml_points;
	}
	
	public String GetLevel(int num){
		switch (num) {
		case 0:
			return level_1;
		case 1:
			return level_2;
		case 2:
			return level_3;
		case 3:
			return level_4;
		case 4:
			return level_5;
		case 5:
			return level_6;
		default:
			return "NONE";
		}
	}
	
	//need to change!!!!!
	public int GetNumOfLevels(){
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
