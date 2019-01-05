
var translationMap =
{
	'en': {
		// Intro
		'm_welcome': 'Welcome',
		'm_nameInput': 'Write your name',
		'm_repeatedLogin': "Are you logging in for the first time?",
			
		// User data
		'm_userdata_intro': "Please input data",
		'm_userdata_label_age': "Select your age",
		'm_userdata_input_age': "Select your age",
		'm_userdata_label_sex': "Select your sex",
		'm_userdata_input_sex_label_m': "Male",
		'm_userdata_input_sex_label_f': "FEEMale",
		'm_userdata_input_sex_label_o': "Dolphin",
		
		// Select task
		'selecttask_txt1': "Matchstick <br /> experiment",
		'selecttask_txt2': "Matching familiar  <br />  figures experiment"
	},
	'si': {
		// Intro
		'm_welcome': 'Dobr dosli',
		'm_nameInput': 'Napis svoj ime',
		'm_repeatedLogin': "Are you logged in for the first time?",
			
		// User data
		'm_userdata_intro': "Please input data",
		'm_userdata_label_age': "Select your age",
		'm_userdata_input_age': "Select your age",
		'm_userdata_label_sex': "Select your sex",
		'm_userdata_input_sex_label_m': "Male",
		'm_userdata_input_sex_label_f': "FEEMale",
		'm_userdata_input_sex_label_o': "Dolphin",
		
		// Select task
		'selecttask_txt1': "Matchstick <br /> experiment",
		'selecttask_txt2': "You know it  <br />  figures experiment"
	},
	'de': {
		// Intro
		'm_welcome': 'Willcommennddddd',
		'm_nameInput': 'Write your daddy',
		'm_repeatedLogin': "Are you logged in for the first time?",
			
		// User data
		'm_userdata_intro': "Please input data",
		'm_userdata_label_age': "Select your age",
		'm_userdata_input_age': "Select your age",
		'm_userdata_label_sex': "Select your sex",
		'm_userdata_input_sex_label_m': "Male",
		'm_userdata_input_sex_label_f': "FEEMale",
		'm_userdata_input_sex_label_o': "Dolphin",
		
		// Select task
		'selecttask_txt1': "Matchstick <br /> experiment",
		'selecttask_txt2': "Matching familiar  <br />  figures experiment"
	},
	'sk': {
		// Intro
		'm_welcome': 'Welcome',
		'm_nameInput': 'Write your name in sk',
		'm_repeatedLogin': "Are you logged in for the first time?",
			
		// User data
		'm_userdata_intro': "Please input data",
		'm_userdata_label_age': "Select your age",
		'm_userdata_input_age': "Select your age",
		'm_userdata_label_sex': "Select your sex",
		'm_userdata_input_sex_label_m': "Male",
		'm_userdata_input_sex_label_f': "FEEMale",
		'm_userdata_input_sex_label_o': "Dolphin",
		
		// Select task
		'selecttask_txt1': "Matchstick <br /> experiment",
		'selecttask_txt2': "Matching familiar  <br />  figures experiment"
	}
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
		console.log(loc);
		if (loc !== undefined && loc !== null)
		{
			loc[locAttr] = translationMap[lang][message];
			console.log("Change message");
		}
	}
}

function translateAllId()
{
	var lang = getLanguage();
	var all = document.getElementsByTagName("*");
	for (var i=0; i < all.length; i++) {
		var elementId = all[i].id;
		if (translationMap[lang][elementId] !== null)
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

