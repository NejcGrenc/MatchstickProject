package grenc.masters.uservalidation.education;

import java.util.HashMap;
import java.util.Map;

/**
 * These options are based on
 * International Standard Classification of Education (version 2011)
 * 
 */
public enum EducationLevel
{
	SELECT    ("Please select"),
	Level_O   ("None"),
	Level_1   ("Primary"),
	Level_2_3 ("Secondary"),
	Level_4_5 ("Tertiary"),
	Level_6   ("Bachelor"),
	Level_7   ("Master"),
	Level_8   ("Doctor");
	
	private String description;
	
	private EducationLevel(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	public static Map<String, String> descriptionMap()
	{
		Map<String, String> descriptionMap = new HashMap<>();
		for (EducationLevel el : EducationLevel.values())
			descriptionMap.put(el.name(), el.description);
		return descriptionMap;
	}
}
