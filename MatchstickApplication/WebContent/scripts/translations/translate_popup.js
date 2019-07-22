
var popupTranslationMap =
{
	'en': {
		'm_closeButton': "Done"
	},
	'si': {
		'm_closeButton': "Zaključi"
	},
	'de': {
		'm_closeButton': "Erledigt"
	},
	'sk': {
		'm_closeButton': "Ukonči"
	}
}



function translatePopup()
{	
	var lang = getLanguage();
	
	if (popupTranslationMap === undefined || popupTranslationMap === null)
	{
		console.log("Cannot load translation map for popup!");
		return;
	}
	
	var translatable = popupTranslationMap[lang];
	for (var elementId in translatable) 
	{		
		var message = popupTranslationMap[lang][elementId];
		if (message !== undefined && message !== null)
		{
			var loc = document.getElementById(elementId);
			if (loc !== undefined && loc !== null)
			{
				loc["innerHTML"] = message;
			}
		}
	}
}

