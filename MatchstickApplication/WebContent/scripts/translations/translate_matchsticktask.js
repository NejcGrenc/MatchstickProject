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
		return "Please wait";
}

function solvingTaskText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Solving task";
	if (lang == "si")
		return "Reševanje naloge";
	if (lang == "de")
		return "Solving task";
	if (lang == "sk")
		return "Solving task";
}

var translationMap =
{
	'en': {
		'm_headerText': "Matchstick experiment"
	},
	'si': {
		'm_headerText': "Vžigalični eksperiment"
	},
	'de': {
		'm_headerText': "Matchstick experiment"
	},
	'sk': {
		'm_headerText': "Matchstick experiment"
	}
}


function tooltipEquationText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Equation";
	if (lang == "si")
		return "Enačba";
	if (lang == "de")
		return "Equation";
	if (lang == "sk")
		return "Equation";
}

function tooltipSolvedText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "is correct.";
	if (lang == "si")
		return "je pravilna.";
	if (lang == "de")
		return "is correct.";
	if (lang == "sk")
		return "is correct.";
}

function tooltipOnlyValidText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "is valid, but not correct.";
	if (lang == "si")
		return "ni pravilna.";
	if (lang == "de")
		return "is valid, but not correct.";
	if (lang == "sk")
		return "is valid, but not correct.";
}

function tooltipContinueText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Contine to next equation";
	if (lang == "si")
		return "Nadaljuj z naslednjo enačbo";
	if (lang == "de")
		return "Contine to next equation";
	if (lang == "sk")
		return "Contine to next equation";
}
