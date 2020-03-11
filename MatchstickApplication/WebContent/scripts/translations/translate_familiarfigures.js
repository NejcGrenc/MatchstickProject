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
		return "Reševanje naloge";
	if (lang == "de")
		return "Lösenaufgabe";
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
		return "Durchschnittliche Zeit pro Aufgabe";
	if (lang == "sk")
		return "Priemerný čas strávený pri jednom obrázku";
}

