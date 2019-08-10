

function getTranslationMap()
{
	if (typeof translationMap === 'undefined' || translationMap === null) 
	{
		console.log("Cannot load translation map!");
		return null;
	}
	return translationMap;
}

function getLanguage()
{
	// Loads language from value set by LanguageBall builder
	var lang = userLang;
	if (lang == null || lang == undefined)
	{
		// Default
		lang = "en";
	}
	return lang;
}

function translate(message, attribute)
{
	if (getTranslationMap() === null)
		return;
	
	console.log("Translating:", message, getLanguage());
	
	var locAttr = "innerHTML";
	if (attribute !== undefined && attribute !== null)
	{
		locAttr = attribute;
	}
	
	if (message !== undefined && message !== null)
	{
		var lang = getLanguage();
		var loc = document.getElementById(message);
		if (loc !== undefined && loc !== null)
		{
			loc[locAttr] = getTranslationMap()[lang][message];
			console.log("Change message");
		}
	}
}

function translateAll()
{
	if (getTranslationMap() === null)
		return;
	
	var lang = getLanguage();
	var translatable = getTranslationMap()[lang];
	for (var elementId in translatable) {
		translate(elementId)
	}
}

function setLanguageBall()
{
	var lang = getLanguage();
	var langBall = document.getElementById('lang_ball');
	if (langBall !== undefined && langBall !== null)
	{
		if (lang == "en")
		{
			langBall.src="images/languages/flag-en.png";
		}
		if (lang == "si")
		{
			langBall.src="images/languages/flag-si.png";
		}
		if (lang == "de")
		{
			langBall.src="images/languages/flag-de.png";
		}
		if (lang == "sk")
		{
			langBall.src="images/languages/flag-sk.png";
		}
	}
	if (langBall.src === undefined || langBall.src === null)
	{ 
		console.log("ERROR: No language specified."); 
	}
}

