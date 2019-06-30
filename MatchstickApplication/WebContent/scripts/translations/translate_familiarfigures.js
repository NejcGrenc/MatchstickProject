function pleaseWaitText() 
{
	var lang = getLanguage();
	if (lang == "en")
		return "Please wait";
	if (lang == "si")
		return "Prosimo počakajte";
	if (lang == "de")
		return "Please wait";
	if (lang == "sk")
		return "Prosím čakajte";
}
	
function solvingTaskText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Solving task";
	if (lang == "si")
		return "Preševanje naloge";
	if (lang == "de")
		return "Solving task";
	if (lang == "sk")
		return "Riešenie úlohy";
}

function resultText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Result";
	if (lang == "si")
		return "Resultat";
	if (lang == "de")
		return "Result";
	if (lang == "sk")
		return "Výsledok";
}

function averageText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Average time per figure";
	if (lang == "si")
		return "Povprečen čas na nalogo";
	if (lang == "de")
		return "Average time per figure";
	if (lang == "sk")
		return "Priemerný čas strávený pri jednom obrázku";
}

function finalText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Done.";
	if (lang == "si")
		return "Konec.";
	if (lang == "de")
		return "Done.";
	if (lang == "sk")
		return "Done.";
}
function finalText2()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Thank you for your participation.";
	if (lang == "si")
		return "Hvala za sodelovanje.";
	if (lang == "de")
		return "Thank you for your participation.";
	if (lang == "sk")
		return "Thank you for your participation.";
}

var translationMap =
{
	'en': {
		'm_headerText': "Matching familiar figures test"
	},
	'si': {
		'm_headerText': "Eksperiment podobnih slik"
	},
	'de': {
		'm_headerText': "Matching familiar figures test"
	},
	'sk': {
		'm_headerText': "Test mapovania podobných obrázkov"
	}
}
