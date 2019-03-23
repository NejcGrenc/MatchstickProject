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
		return "Solving task";
	if (lang == "sk")
		return "Riešenie úlohy";
}

function learningTaskText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Learning task";
	if (lang == "si")
		return "Učenje naloge";
	if (lang == "de")
		return "Learning task";
	if (lang == "sk")
		return "Učenie úlohy";
}

function observingTaskText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Observing task";
	if (lang == "si")
		return "Opazovanje naloge";
	if (lang == "de")
		return "Observing task";
	if (lang == "sk")
		return "Pozorovanie úlohy";
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
		'm_headerText': "Experiment so zápalkami"
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
		return "Rovnica";
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
		return "je správna.";
}

function tooltipOnlyValidText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "is valid, but not correct.";
	if (lang == "si")
		return "je veljavna, a ni pravilna.";
	if (lang == "de")
		return "is valid, but not correct.";
	if (lang == "sk")
		return "je platná, ale nie je správna.";
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
		return "Pokračujte ďalej";
}
