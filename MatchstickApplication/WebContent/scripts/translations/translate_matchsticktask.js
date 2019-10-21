function pleaseWaitText() 
{
	var lang = getLanguage();
	if (lang == "en")
		return "Please wait";
	if (lang == "si")
		return "Prosimo počakajte";
	if (lang == "de")
		return "Bitte warten";
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
		return "Aufgabe lösen";
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
		return "Lernaufgabe";
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
		return "Beobachtungsaufgabe";
	if (lang == "sk")
		return "Pozorovanie úlohy";
}

function tooltipEquationText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Equation";
	if (lang == "si")
		return "Enačba";
	if (lang == "de")
		return "Gleichung";
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
		return "ist richtig.";
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
		return "ist gültig, aber nicht korrekt.";
	if (lang == "sk")
		return "je platná, ale nie je správna.";
}

function tooltipContinueText()
{
	var lang = getLanguage();
	if (lang == "en")
		return "Contine to next equation";
	if (lang == "si")
		return "Nadaljujte z naslednjo enačbo";
	if (lang == "de")
		return "Weiter zur nächsten Gleichung";
	if (lang == "sk")
		return "Pokračujte na nasledujúcu rovnicu";
}

function restrictionsText(onlyOneMoveRestricted, blocked)
{
	console.log("restrictionsText", onlyOneMoveRestricted, blocked);
	var lang = getLanguage();
	if (onlyOneMoveRestricted)
	{
		if (blocked)
		{
			if (lang == "en")
				return "Wrong move. PLEASE START AGAIN.";
			if (lang == "si")
				return "Napačna poteza. PROSIMO ZAČNITE PONOVNO.";
			if (lang == "de")
				return "Falscher Zug. BITTE BEGINNEN SIE WIEDER.";
			if (lang == "sk")
				return "Nesprávny ťah. PROSÍM ZAČÍNAJTE ZNOVA.";
		}
		else
		{
			if (lang == "en")
				return "Correct the equation by USING ONLY ONE MOVE.";
			if (lang == "si")
				return "Popravite enačbo z UPORABO LE ENE POZTEZE.";
			if (lang == "de")
				return "Korrigieren Sie die Gleichung, indem Sie NUR EINEN ZUG VERWENDEN.";
			if (lang == "sk")
				return "Opravte rovnicu tak, že POUŽIJETE IBA JEDEN ŤAH.";
		}
	}
	else
	{
		if (lang == "en")
			return "Correct the equation by USING THE LEAST NUMBER OF MOVES.";
		if (lang == "si")
			return "Popravite enačbo z UPORABO NAJMANJŠEGA ŠTEVILA POTEZ.";
		if (lang == "de")
			return "Korrigieren Sie die Gleichung, indem Sie DIE LETZTE ANZAHL VON ZÜGEN VERWENDEN.";
		if (lang == "sk")
			return "Opravte rovnicu tak, že POUŽIJETE ČO NAJNIŽŠÍ POČET ŤAHOV.";
	}
}


