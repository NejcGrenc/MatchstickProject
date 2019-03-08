function popupLine1Text() 
{
	var lang = getLanguage();
	if (lang == "en")
		return "Performing experiments as: ";
	if (lang == "si")
		return "Izvajanje eksperimenta kot: ";
	if (lang == "de")
		return "Performing experiments as: ";
	if (lang == "sk")
		return "Vykon�vanie experimentov ako: ";
}


function translatePopup()
{	
	var locAttr = "innerHTML";
	var loc = document.getElementById('m_popup_firstline');
	if (loc !== undefined && loc !== null)
	{
		loc[locAttr] = popupLine1Text() ;
	}
}
