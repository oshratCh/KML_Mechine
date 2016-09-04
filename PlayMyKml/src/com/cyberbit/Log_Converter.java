package com.cyberbit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Log_Converter {
	
	public Map<String, List<Map<String, String>>> GetPointsByServiceName(String pathname){
		List<Map<String, String>> rawFileData = parseFileData(pathname);
		Map<String, List<Map<String, String>>> points_array_after_groupby = parseRawFileData("SERVICE_NAME", rawFileData);
		return points_array_after_groupby;
	}
	
	private List<Map<String, String>> parseFileData(String pathname) {
		Boolean isStartOfSection = false;;
		List<Map<String, String>> sections = new ArrayList<Map<String, String>>();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathname));
			Map<String, String> sectionContent = null;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("------------------------")) {
					isStartOfSection = true;
					if (sectionContent != null) {
						//Check if the abstraction is a location 
						if((sectionContent.get("MAIL_FROM_LOCATION_LONGITUDE")!=null)&&
								(sectionContent.get("MAIL_FROM_LOCATION_LATITUDE")!=null))
						sections.add(sectionContent);
					}
					sectionContent = new HashMap<String, String>();

				} else {
					if (sectionContent != null) {
						if (isStartOfSection) {
							sectionContent.put("SERVICE_NAME", line);
							isStartOfSection = false;
						}

						if (line.split(":").length > 1) {
							String[] content = line.split(":");
							sectionContent.put(content[0].trim(),
									content[1].trim());
						}
					}
				}
			}
			//Add the last point to the map
			if (sectionContent != null) {
				if((sectionContent.get("MAIL_FROM_LOCATION_LONGITUDE")!=null)&&
						(sectionContent.get("MAIL_FROM_LOCATION_LATITUDE")!=null))
				sections.add(sectionContent);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sections;
	}

	public Map<String, List<Map<String, String>>> parseRawFileData(String groupName, List<Map<String, String>> rawFileData) {

		int size = rawFileData.size();

		Map<String, List<Map<String, String>>> groupedData = new HashMap<String, List<Map<String, String>>>();

		for (int i = 0; i < size; i++) {
			Map<String, String> content = rawFileData.get(i);
			String folderGroupName = content.get(groupName);
			List<Map<String, String>> aList = groupedData.get(folderGroupName);
			if (aList == null) {
				aList = new ArrayList<Map<String, String>>();

				groupedData.put(folderGroupName, aList);

			}

			aList.add(content);
		}

		appendDesciprtion(groupedData);
		return groupedData;
	}
	
	private void appendDesciprtion( Map<String, List<Map<String, String>>> groupedData) {
     // add to each point <![CDATA[<h3> Date : 2014-04-02 14:38:06</h3> <h3>
     // SRC IP : 10.197.168.111</h3>]]>
     Set<String> allGroups = groupedData.keySet();
     DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ", Locale.US);
     for (String aGroup : allGroups) {
            List<Map<String, String>> listOfPlacemarks = groupedData.get(aGroup);
            for (Map<String, String> aPlacemark : listOfPlacemarks) {
                  StringBuffer descriptionBuff = new StringBuffer("<![CDATA[");
                  String time = aPlacemark.get("INTERCEPTION_INFO_INTERCEPTION_TIME");
                  java.util.Date jDate = new java.util.Date(Long.parseLong(time));
                  descriptionBuff.append("<h3>").append(" Date: ").append("<span style='font-weight:normal'>").append(df.format(jDate)).append("</span>").append("</h3>");

                  String sourceId = aPlacemark.get("INTERCEPTION_INFO_CLIENT_IP");
                  descriptionBuff.append("<h3>").append("SRC IP : ").append("<span style='font-weight:normal'>").append(sourceId).append("</span>").append("</h3>");
                  String httpDomainName = aPlacemark.get("HTTP_DOMAIN_NAME");
                  descriptionBuff.append("<h3>").append("Domain Name : ").append("<span style='font-weight:normal'>").append(httpDomainName).append("</span>").append("</h3>");
                  String userAgent = aPlacemark.get("USER_AGENT");
                  descriptionBuff.append("<h3>").append("User Agent : ").append("<span style='font-weight:normal'>").append(userAgent).append("</span>").append("</h3>");
                  aPlacemark.put("description", descriptionBuff.toString());
            }
     }
	}
		
}
