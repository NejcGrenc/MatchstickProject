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
	SELECT      ("Please select",		"Prosimo izberite",		"Prosíme vyberte",		"Bitte auswählen"	),
	Level_O     ("No education",		"Brez izobrazbe",		"Bez vzdelania",		"Ohne Ausbildung"	),
	Level_1     ("Primary school",		"Osnovna šola",			"Základná škola",		"Grundschule"		),
	Level_2_3   ("Secondary school",	"Srednja šola",			"Stredná škola",		"Weiterführende Schule"		),
	Level_4_5_6 ("Bachelor degree / Tertiary school",	"Univerzitetna diploma / Višja šola",	"Bakalárske titul",		"Bachelor-Abschluss / Hochschule"),
	Level_7     ("Master degree",		"Magisterij",			"Magisterský titul",	"Master-Abschluss"	),
	Level_8     ("Doctorate", 			"Doktorat", 			"Doktorát", 			"Doktor"			);
	
	private String descriptionEN;
	private String descriptionSI;
	private String descriptionSK;
	private String descriptionDE;
	
	private EducationLevel(String descriptionEN, String descriptionSI, String descriptionSK, String descriptionDE)
	{
		this.descriptionEN = descriptionEN;
		this.descriptionSI = descriptionSI;
		this.descriptionSK = descriptionSK;
		this.descriptionDE = descriptionDE;
	}
	
	
	public static Map<String, String> descriptionMap(String lang)
	{
		Map<String, String> descriptionMap = new HashMap<>();
		for (EducationLevel el : EducationLevel.values())
		{
			String desc;
			switch (lang) 
			{
				default:
				case "en":
					desc = el.descriptionEN; break;
				case "si":
					desc = el.descriptionSI; break;
				case "sk":
					desc = el.descriptionSK; break;
				case "de":
					desc = el.descriptionDE; break;
			}
			
			descriptionMap.put(el.name(), desc);
		}			
		return descriptionMap;
	}
	
}
