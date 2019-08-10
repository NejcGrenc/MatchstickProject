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
	SELECT    ("Please select",		"Prosimo izberite",		"Pros�mo vyberte",		"Bitte ausw�hlen"	),
	Level_O   ("None", 				"Brez", 				"Bez", 					"Keiner"			),
	Level_1   ("Primary school",	"Osnovna �ola",			"Z�kladn� �kola",		"Grundschule"		),
	Level_2_3 ("Secondary school",	"Srednja �ola",			"Stredn� �kola",		"Weiterf�hrende Schule"		),
	Level_4_5 ("Tertiary school",	"Vi�ja �ola",			"Vy��ia �kola",			"H�here Schule"		),
	Level_6   ("Bachelor degree",	"Univerzitetna diploma","Bakal�rske �t�dium",	"Bachelor-Studium" 	),
	Level_7   ("Master degree",		"Magisterij",			"Magistersk� titul",	"Master-Abschluss"	),
	Level_8   ("Doctorate", 		"Doktorat", 			"Doktor�t", 			"Doktor"			);
	
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
